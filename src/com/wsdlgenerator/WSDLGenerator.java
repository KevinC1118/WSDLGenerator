/**
 * 
 */
package com.wsdlgenerator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import javax.wsdl.Binding;
import javax.wsdl.BindingInput;
import javax.wsdl.BindingOperation;
import javax.wsdl.BindingOutput;
import javax.wsdl.Definition;
import javax.wsdl.Input;
import javax.wsdl.Message;
import javax.wsdl.Operation;
import javax.wsdl.OperationType;
import javax.wsdl.Output;
import javax.wsdl.Part;
import javax.wsdl.Port;
import javax.wsdl.PortType;
import javax.wsdl.Service;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.soap.SOAPAddress;
import javax.wsdl.extensions.soap.SOAPBinding;
import javax.wsdl.extensions.soap.SOAPBody;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLWriter;
import javax.xml.namespace.QName;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import com.google.appengine.api.datastore.Blob;
import com.ibm.wsdl.extensions.soap.SOAPAddressImpl;
import com.ibm.wsdl.extensions.soap.SOAPBindingImpl;
import com.ibm.wsdl.extensions.soap.SOAPBodyImpl;
import com.wsdlgenerator.model.ExcelFile;
import com.wsdlgenerator.model.GeneratedFile;

/**
 * @author Kevin.C
 * 
 */
public class WSDLGenerator extends AbstractGenerator {

	private static final Logger LOGGER = Logger.getLogger(WSDLGenerator.class
			.getName());
	private List<GeneratedFile> wsdlFiles = new ArrayList<GeneratedFile>();

	private WSDLFactory wsdlFactory;
	private String fileName;

	{
		try {
			wsdlFactory = WSDLFactory.newInstance();
		} catch (WSDLException e) {
			LOGGER.warning(e.toString());
		}
	}

	/**
	 * @param dataSource
	 */
	public WSDLGenerator(List<ExcelFile> excelFiles, Properties prop) {
		super(excelFiles, prop);
		execute();
	}

	private void execute() {

		// if(this.destination == null) throw new
		// InterruptedException("File not found.");
		for (ExcelFile ef : super.getExcelFiles()) {

			fileName = ef.getName();

			Set<String> sn = ef.getServices();
			Iterator<String> iterator = sn.iterator();

			String str;
			while (iterator.hasNext()) {
				str = iterator.next();

				Definition definition = namespaceGen(str);
				// typesGen(definition, str);
				messageGen(definition, str);
				portTypeGen(definition, str);
				bindingGen(definition, str);
				serviceGen(definition, str);
				save(definition, str);
			}
		}
	}

	@Override
	public List<GeneratedFile> getGeneratedFiles() {
		return wsdlFiles;
	}

	private void save(Definition definition, String serviceName) {

		GeneratedFile generatedFile = new GeneratedFile();

		WSDLWriter writer;

		try {
			writer = wsdlFactory.newWSDLWriter();

			ByteArrayOutputStream stream = new ByteArrayOutputStream();

			writer.writeWSDL(definition, stream);

			generatedFile.setBlob(new Blob(insertSchemaImport(
					stream.toByteArray(), definition, serviceName)));
			generatedFile.setName(new StringBuffer(definition.getQName()
					.getLocalPart()).append('_').append(serviceName)
					.append(".wsdl").toString());
			wsdlFiles.add(generatedFile);

			/*LOGGER.info(String.format("Generate %s", generatedFile.getName()));*/

		} catch (WSDLException e) {
			LOGGER.warning(e.toString());
		} catch (Exception e) {
			LOGGER.warning(e.toString());
		}
	}

	// service name was retrieved from excel file
	private Definition namespaceGen(String serviceName) {

		// Create a new definition
		Definition definition = wsdlFactory.newDefinition();

		// Set TargetNamespace
		definition.setTargetNamespace(new StringBuffer(getProperty()
				.getProperty("excel2wsdl.targetnamespace.urlprefix"))
				.append(fileName).append(".").append(serviceName.toUpperCase())
				.append(".wsdl").toString());

		definition.addNamespace("soap",
				getProperty().getProperty("excel2wsdl.namespace.soap"));
		definition.addNamespace("wsdl",
				getProperty().getProperty("excel2wsdl.namespace.wsdl"));
		definition.addNamespace("xsd",
				getProperty().getProperty("excel2wsdl.namespace.xsd"));
		definition.addNamespace("tns", definition.getTargetNamespace());
		definition
				.setQName(new QName(definition.getTargetNamespace(), fileName));

		/* 
		 * Generate each service's namespace, it was equal to schame namespace.
		 * example:
		 * http://{lancer.namespace.url}/UC_DIS_SALESORDER.QRYSALESORDER
		 */
		definition.addNamespace(
				serviceName.trim().toLowerCase(),
				new StringBuffer(getProperty().getProperty(
						"excel2wsdl.targetnamespace.urlprefix")).append(fileName)
						.append(".").append(serviceName.trim().toUpperCase())
						.toString());

		return definition;
	}

	private void messageGen(Definition definition, String serviceName) {

		Map<?, ?> namespace = definition.getNamespaces();

		Message message;
		Part part;

		// Request

		message = definition.createMessage();
		part = definition.createPart();

		part.setName("request");
		part.setElementName(new QName(namespace.get(serviceName.toLowerCase())
				.toString(), new StringBuffer(definition.getQName()
				.getLocalPart()).append(".").append(serviceName.toUpperCase())
				.toString()));
		message.addPart(part);
		// set message's name
		message.setQName(new QName(namespace.get("tns").toString(),
				new StringBuffer(serviceName.toUpperCase()).append(
						getProperty().getProperty(
								"excel2wsdl.message.request.suffix"))
						.toString()));
		message.setUndefined(false);

		definition.addMessage(message);

		// Response

		message = definition.createMessage();
		part = definition.createPart();

		part.setName("response");
		part.setElementName(new QName(namespace.get(serviceName.toLowerCase())
				.toString(), new StringBuffer(definition.getQName()
				.getLocalPart()).append('.').append(serviceName.toUpperCase())
				.append("Response").toString()));
		message.addPart(part);
		message.setQName(new QName(namespace.get("tns").toString(),
				new StringBuffer(serviceName.toUpperCase()).append(
						getProperty().getProperty(
								"excel2wsdl.message.response.suffix"))
						.toString()));
		message.setUndefined(false);

		definition.addMessage(message);

	}

	private void portTypeGen(Definition definition, String serviceName) {

		Map<?, ?> message = definition.getMessages();
		Map<?, ?> namespace = definition.getNamespaces();

		PortType portType;
		Operation operation;
		Input input;
		Output output;

		portType = definition.createPortType();
		operation = definition.createOperation();
		input = definition.createInput();
		output = definition.createOutput();

		input.setMessage((Message) message.get(new QName(namespace.get("tns")
				.toString(), new StringBuffer(serviceName).append(
				getProperty().getProperty("excel2wsdl.message.request.suffix"))
				.toString())));
		output.setMessage((Message) message.get(new QName(namespace.get("tns")
				.toString(), new StringBuffer(serviceName)
				.append(getProperty().getProperty(
						"excel2wsdl.message.response.suffix")).toString())));

		operation.setInput(input);
		operation.setOutput(output);
		operation.setName(serviceName.toUpperCase());
		operation.setStyle(OperationType.REQUEST_RESPONSE);
		operation.setUndefined(false);

		portType.addOperation(operation);
		portType.setQName(new QName(namespace.get("tns").toString(),
				serviceName.toUpperCase()));
		portType.setUndefined(false);

		definition.addPortType(portType);
	}

	private void bindingGen(Definition definition, String serviceName) {

		Map<?, ?> portTypes = definition.getAllPortTypes();
		Map<?, ?> namespace = definition.getNamespaces();

		Binding binding;
		SOAPBinding soapBinding;
		BindingOperation bindingOperation;
		BindingInput bindingInput;
		BindingOutput bindingOutput;
		SOAPBody soapBody;

		soapBinding = new SOAPBindingImpl();
		soapBinding.setStyle(getProperty().getProperty(
				"excel2wsdl.soapbinding.style"));
		soapBinding.setTransportURI(getProperty().getProperty(
				"excel2wsdl.soapbinding.transport"));

		soapBody = new SOAPBodyImpl();
		soapBody.setUse(getProperty().getProperty("excel2wsdl.soapbody.use"));

		bindingInput = definition.createBindingInput();
		bindingInput.addExtensibilityElement(soapBody);

		bindingOutput = definition.createBindingOutput();
		bindingOutput.addExtensibilityElement(soapBody);

		binding = definition.createBinding();

		bindingOperation = definition.createBindingOperation();
		bindingOperation.setName(serviceName.toUpperCase());
		bindingOperation.setBindingInput(bindingInput);
		bindingOperation.setBindingOutput(bindingOutput);

		binding.setQName(new QName(namespace.get("tns").toString(),
				new StringBuffer(serviceName.toUpperCase()).append(
						getProperty().getProperty("excel2wsdl.binding.suffix"))
						.toString()));
		binding.setPortType((PortType) portTypes.get(new QName(namespace.get(
				"tns").toString(), serviceName)));
		binding.addExtensibilityElement(soapBinding);
		binding.addBindingOperation(bindingOperation);
		binding.setUndefined(false);

		definition.addBinding(binding);
	}

	private void serviceGen(Definition definition, String serviceName) {

		Map<?, ?> bindings = definition.getAllBindings();
		Map<?, ?> namespace = definition.getNamespaces();

		Service service;
		Port port;
		SOAPAddress soapAddress;

		service = definition.createService();
		port = definition.createPort();

		soapAddress = new SOAPAddressImpl();
		soapAddress.setLocationURI(new StringBuffer(getProperty().getProperty(
				"excel2wsdl.soapaddress.location"))
				.append(definition.getQName().getLocalPart()).append('_')
				.append(serviceName.toUpperCase()).toString());

		port.addExtensibilityElement(soapAddress);
		port.setBinding((Binding) bindings.get(new QName(namespace.get("tns")
				.toString(), new StringBuffer(serviceName.toUpperCase())
				.append(getProperty().getProperty("excel2wsdl.binding.suffix"))
				.toString())));

		port.setName(new StringBuffer(definition.getQName().getLocalPart())
				.append('_').append(serviceName.toUpperCase()).toString());

		service.addPort(port);
		service.setQName(new QName(namespace.get("tns").toString(),
				new StringBuffer(serviceName.toUpperCase()).append(
						getProperty().getProperty("excel2wsdl.service.suffix"))
						.toString()));

		definition.addService(service);
	}

	private byte[] insertSchemaImport(byte[] bs, Definition definition,
			String serviceName) {
		Map<?, ?> namespace = definition.getNamespaces();
		XMLOutputter xmlOutputter = new XMLOutputter();
		SAXBuilder builder = new SAXBuilder();

		ByteArrayInputStream inputStream = new ByteArrayInputStream(bs);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		try {
			Document document = builder.build(inputStream);

			Element types = new Element("types", "wsdl", getProperty()
					.getProperty("excel2wsdl.namespace.wsdl"));

			Element schema = new Element("schema", "xsd", getProperty()
					.getProperty("excel2wsdl.namespace.xsd"));
			Element schemaImport = new Element("import", "xsd", getProperty()
					.getProperty("excel2wsdl.namespace.xsd"));
			schemaImport.setAttribute("namespace",
					namespace.get(serviceName.toLowerCase().trim()).toString());
			schemaImport.setAttribute("schemaLocation",
					new StringBuffer(definition.getQName().getLocalPart())
							.append('_').append(serviceName).append(".xsd")
							.toString());
			schema.addContent(schemaImport);

			types.addContent(schema);

			document.getRootElement().addContent(0, types);
			xmlOutputter.output(document, outputStream);
			return outputStream.toByteArray();
		} catch (IOException e) {
			LOGGER.warning(e.toString());
		} catch (JDOMException e) {
			LOGGER.warning(e.toString());
		}

		return null;
	}
}
