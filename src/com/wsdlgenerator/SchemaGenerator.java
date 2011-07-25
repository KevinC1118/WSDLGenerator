/**
 * 
 */
package com.wsdlgenerator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.XmlCursor;
import org.w3.x2001.xmlSchema.AllNNI;
import org.w3.x2001.xmlSchema.Element;
import org.w3.x2001.xmlSchema.ExplicitGroup;
import org.w3.x2001.xmlSchema.SchemaDocument;
import org.w3.x2001.xmlSchema.SchemaDocument.Schema;
import org.w3.x2001.xmlSchema.TopLevelElement;

import com.google.appengine.api.datastore.Blob;
import com.wsdlgenerator.model.ExcelFile;
import com.wsdlgenerator.model.GeneratedFile;
import com.wsdlgenerator.model.RowObject;
import com.wsdlgenerator.util.CommonUtil;

/**
 * @author KevinC
 * 
 */
public class SchemaGenerator extends AbstractGenerator {

	private List<GeneratedFile> schemaFiles = new ArrayList<GeneratedFile>();
	private static final Logger LOGGER = Logger.getLogger(SchemaGenerator.class
			.getName());
	private SchemaDocument schemaDocument;
	private ParentStackDAO parentStackDAO;
	private String _msgName;
	private ExcelFile ef;

	private CommonUtil util = new CommonUtil();

	/**
	 * @param dataSource
	 */
	public SchemaGenerator(List<ExcelFile> excelFiles, Properties prop) {
		super(excelFiles, prop);
		execute();
	}

	private void execute() {

		for (Iterator<ExcelFile> ite = super.getExcelFiles().iterator(); ite
				.hasNext();) {

			ef = ite.next();

			Iterator<String> services = ef.getServices().iterator();

			// String msgName;
			String tmp;
			while (services.hasNext()) {

				schemaDocument = SchemaDocument.Factory.newInstance();
				_msgName = services.next();

				// ex: UC_DIS_SALESORDER.QRYSALESORDER
				namespaceGenerate(new StringBuffer(ef.getName()).append(".")
						.append(_msgName).toString());

				tmp = new StringBuffer(ef.getName()).append(".")
						.append(_msgName).toString();

				if (requestMsgGenerate(tmp) & responseMsgGenerate(tmp))
					save(new StringBuffer(ef.getName()).append("_")
							.append(_msgName).toString());
				else {
					String msg = String
							.format("For some reasons, %s generation failed. Please check %s service in %s.",
									new Object[] { _msgName, _msgName,
											ef.getName() });
					LOGGER.warning(msg);
					getERRORMSG().add(msg);
				}
			}
		}
	}

	private void save(String fileName) {

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			schemaDocument.save(stream);
			GeneratedFile generatedFile = new GeneratedFile();
			generatedFile.setBlob(new Blob(stream.toByteArray()));
			generatedFile.setName(new StringBuffer(fileName).append(".xsd")
					.toString());
			schemaFiles.add(generatedFile);

			/*LOGGER.info(String.format("Generate %s", generatedFile.getName()));*/

		} catch (IOException e) {
			LOGGER.warning(e.toString());
		}
	}

	@Override
	public List<GeneratedFile> getGeneratedFiles() {
		return schemaFiles;
	}

	/**
	 * 
	 * @param msgName
	 *            format eg. UC_DIS_SALESORDER.QRYSALESORDER
	 */
	private void namespaceGenerate(String msgName) {

		Schema schema = schemaDocument.addNewSchema();
		schema.setTargetNamespace(new StringBuffer(getProperty().getProperty(
				"excel2wsdl.targetnamespace.urlprefix")).append(msgName)
				.toString());

		XmlCursor xmlCursor = schemaDocument.newCursor();

		if (xmlCursor.toFirstChild() & xmlCursor.toFirstAttribute()) {
			xmlCursor.insertNamespace("tns", schema.getTargetNamespace());
			xmlCursor.insertNamespace("",
					getProperty().getProperty("excel2wsdl.namespace.xsd"));
		}
	}

	/**
	 * 產生request message schema
	 * 
	 * @return boolean 是否成功產生request message schema
	 */
	private boolean requestMsgGenerate(String msgName) {
		if (schemaDocument == null)
			return false;
		if (schemaDocument.getSchema() == null)
			return false;

		// 初始化存放父節點的堆疊
		parentStackDAO = new ParentStackDAO();

		Schema schema = schemaDocument.getSchema();

		try {
			addTopLevelElement(schema, msgName, (ArrayList<?>) ef
					.getRequestMsg().get(msgName), null);
		} catch (Exception e) {
			LOGGER.warning(e.toString());
			return false;
		}
		return true;

	}

	private boolean responseMsgGenerate(String msgName) {

		if (schemaDocument == null)
			return false;
		if (schemaDocument.getSchema() == null)
			return false;

		// 初始化存放父節點的堆疊
		parentStackDAO = new ParentStackDAO();

		Schema schema = schemaDocument.getSchema();
		String responseMsgName = new StringBuffer(msgName).append("Response")
				.toString();
		try {
			addTopLevelElement(schema, responseMsgName, (ArrayList<?>) ef
					.getResponseMsg().get(responseMsgName), null);
		} catch (Exception e) {
			LOGGER.warning(e.toString());
			return false;
		}
		return true;
	}

	/**
	 * Add a new top level element. It's always a complex type element.
	 * 
	 * @param schema
	 *            Schema
	 * @param ElementName
	 *            Element's name
	 * @param elementList
	 *            Element list
	 * 
	 */
	private void addTopLevelElement(Schema schema, String elementName,
			ArrayList<?> elementList, RowObject parentRowObject) {

		// 作為遞迴時傳給自己的array list
		ArrayList<?> elList;
		TopLevelElement element;
		ExplicitGroup sequence;
		RowObject rowObject;

		elList = (ArrayList<?>) elementList.clone();
		element = schema.addNewElement();

		element.setName(elementName);
		sequence = element.addNewComplexType().addNewSequence();

		// 如果parent stack沒有任何一個元素, 則加入一個空白且index為0的element作為root parent
		if (parentRowObject == null)
			parentStackDAO.init(element);
		else {
			ParentStack parentStack = new ParentStack();
			parentStack.setRowObject(parentRowObject);
			parentStack.setElement(element);
			parentStackDAO.add(parentStack);
		}

		/*
		 * 不同階層的element會獨立成一個top element, 因此element
		 * list的第一個element的level可代表這個階層所屬的level
		 */
		int currentLevel;
		try {
			currentLevel = ((RowObject) elementList.get(0)).getLevel();
		} catch (IndexOutOfBoundsException e) {
			LOGGER.warning(String.format("%s : %s", elementName, e.toString()));
			currentLevel = 1;
		}

		Element newOne;

		for (int i = 0, max = elementList.size(); i < max; i++) {

			rowObject = (RowObject) elementList.get(i);

			/*
			 * 如果"不是"LIST OF HASHMAP, 則建立新的simple element 如果"是"LIST OF HASHMAP,
			 * 則呼叫自己(遞迴)
			 */
			if (!Pattern.matches("^LIST.*HASHMAP$", rowObject.getType().trim())) {

				/*
				 * 當rowObject的type為LIST OF HASHMAP, 下一個rowObject必屬於子階層;
				 * 因此這裡只需要關心往下探尋時,是否回到父階層
				 */
				if (rowObject.getLevel() < currentLevel) { // 目前處理的rowObject小於當前的階層,代表回到父階層

					Element parentEl;

					// 取得parent element
					parentEl = parentStackDAO
							.get(parentStackDAO.size()
									- (currentLevel - rowObject.getLevel() + 1))
							.getElement();

					// 指向parent element的sequence節點
					sequence = parentEl.getComplexType().getSequence();

					addLocalElement(sequence.addNewElement(), rowObject);

					// 移除list內已經產生element的rowObject
					elList.remove(0);
				} else {

					addLocalElement(sequence.addNewElement(), rowObject);

					// 移除list內已經產生element的rowObject
					elList.remove(0);
				}

			} else { // Is LIST OF HASHMAP type

				// If new List of hashmap item was more top than previous's.
				if (rowObject.getLevel() < currentLevel) {

					Element parentEl;

					// 取得parent element
					parentEl = parentStackDAO
							.get(parentStackDAO.size()
									- (currentLevel - rowObject.getLevel() + 1))
							.getElement();

					// LOGGER.info(String.format("%s's parent is %s",
					// rowObject.getKey(), parentEl.getName()));
					// 指向parent element的sequence節點
					sequence = parentEl.getComplexType().getSequence();

					elementName = parentEl.getName();

				}

				newOne = sequence.addNewElement();
				newOne.setRef(new QName(schema.getTargetNamespace(),
						new StringBuffer(elementName).append(".")
								.append(rowObject.getKey()).toString(), "tns"));

				generateRefElement(rowObject, newOne);

				// 移除list內已經產生element的rowObject
				elList.remove(0);

				// 遞迴
				addTopLevelElement(schema, new StringBuffer(elementName)
						.append(".").append(rowObject.getKey()).toString(),
						elList, rowObject);

				// 結束迴圈
				break;
			}
		}

		elList = null;
	}

	/**
	 * 
	 * @param element
	 * @param rowObject
	 */
	private void addLocalElement(Element element, RowObject rowObject) {

		element.setName(rowObject.getKey().trim());

		String type = rowObject.getType().trim();
		QName qType;

		try {
			qType = util.getSchemaSimpleType(type);
		} catch (UnknownTypeException e) {
			
			getERRORMSG().add(
					String.format(
							"%s < %s < %s < %s",
							new Object[] { e.getMessage(), element.getName(), _msgName,
									ef.getName() }));
			
			qType = new QName(getProperty().getProperty(
					"excel2wsdl.namespace.xsd"), "");
		}

		/*
		 * 判斷是否為LIST型態 是, maxOccurs="unbounded" 否, maxOccurs="1" - default
		 */
		if (Pattern.matches("^LIST.*", type)) { // LIST type

			element.setType(qType);
			generateRefElement(rowObject, element);

		} else { // Not LIST type

			element.setType(qType);
			generateSimpleElement(rowObject, element);
		}
	}

	/**
	 * Generate a simple type element.
	 * 
	 * @param rowObject
	 * @param element
	 */
	private void generateSimpleElement(RowObject rowObject, Element element) {

		if (Boolean.parseBoolean(getProperty().getProperty(
				"excel2wsdl.hasNecessaryValue"))) {
			if (rowObject.isNecessary()) { // required
				element.setMinOccurs(new BigInteger("1"));
				element.setMaxOccurs(1);
			} else { // non-required
				element.setMinOccurs(new BigInteger("0"));
				element.setMaxOccurs(1);
			}
		} else {
			element.setMinOccurs(new BigInteger("0"));
			element.setMaxOccurs(1);
		}
	}

	/**
	 * Generate a reference element.
	 * 
	 * @param rowObject
	 * @param element
	 */
	private void generateRefElement(RowObject rowObject, Element element) {
		// 有必傳值
		if (Boolean.parseBoolean(getProperty().getProperty(
				"excel2wsdl.hasNecessaryValue"))) {
			if (rowObject.isNecessary()) { // 必傳
				element.setMinOccurs(new BigInteger("1"));
				element.setMaxOccurs(AllNNI.Member.UNBOUNDED);
			} else { // 非必傳
				element.setMinOccurs(new BigInteger("0"));
				element.setMaxOccurs(AllNNI.Member.UNBOUNDED);
			}
		} else { // 沒有必傳值
			element.setMinOccurs(new BigInteger("0"));
			element.setMaxOccurs(AllNNI.Member.UNBOUNDED);
		}
	}

	/**
	 * ParentStack Object inner class
	 * 
	 * @author Kevin.C
	 * 
	 */
	private class ParentStack {

		// 需要rowObject用來判斷level
		private RowObject rowObject;
		private Element element;

		/**
		 * @return the rowObject
		 */
		public RowObject getRowObject() {
			return rowObject;
		}

		/**
		 * @return the element
		 */
		public Element getElement() {
			return element;
		}

		/**
		 * @param rowObject
		 *            the rowObject to set
		 */
		public void setRowObject(RowObject rowObject) {
			this.rowObject = rowObject;
		}

		/**
		 * @param element
		 *            the element to set
		 */
		public void setElement(Element element) {
			this.element = element;
		}
	}

	/**
	 * ParentStack DAO inner class
	 * 
	 * @author Kevin.C
	 * 
	 */
	private class ParentStackDAO {

		private List<ParentStack> parentStacks;

		/**
		 * When parentStacks initialized, it's will add a new parentStack object
		 * that its' RowObject's level is 0 and its' element is root element.
		 * 
		 * @param element
		 */
		public ParentStackDAO() {
			parentStacks = new ArrayList<ParentStack>();
		}

		public void init(Element element) {
			ParentStack parentStack = new ParentStack();
			parentStack.setRowObject(new RowObject(0));
			parentStack.setElement(element);

			parentStacks.add(parentStack);
		}

		public void add(ParentStack parentStack) {
			int level = findSameLevelParent(parentStack);
			if (level != 0) {
				parentStacks.set(level, parentStack);
			} else {
				parentStacks.add(parentStack);
			}
		}

		public ParentStack get(int index) {
			return parentStacks.get(index);
		}

		public int size() {
			return parentStacks.size();
		}

		/*
		 * 如果堆疊中最頂部的parent level與rowObject的level相同, 則刪除替換之; 否, 則直接加入堆疊.
		 * 此堆疊的作用是當element list往下探尋時 當前的rowObject的level小於前一個rowObject時,
		 * 能找所屬的parent element
		 */
		private int findSameLevelParent(ParentStack parentStack) {

			int result = 0;

			if (parentStack.getRowObject().getLevel() < parentStacks.size())
				result = parentStack.getRowObject().getLevel();

			return result;
		}
	}
}
