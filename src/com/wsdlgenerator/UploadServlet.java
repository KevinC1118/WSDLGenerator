package com.wsdlgenerator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.quota.QuotaService;
import com.google.appengine.api.quota.QuotaServiceFactory;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.wsdlgenerator.model.ExcelFile;
import com.wsdlgenerator.model.GeneratedFile;
import com.wsdlgenerator.util.CommonUtil;
import com.wsdlgenerator.util.ExcelParser;
import com.wsdlgenerator.util.MyProperties;

/**
 * @author Kevin.C
 * 
 */

public class UploadServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4686314267448696575L;
	private static final Logger LOGGER = Logger.getLogger(UploadServlet.class
			.getName());

	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Properties prop = MyProperties.getProperties();
		JsonObject jsonObject = new JsonObject();
		JsonArray jsonArray = new JsonArray(); // ERROR message

		QuotaService quotaService = QuotaServiceFactory.getQuotaService();
		long start = quotaService.getCpuTimeInMegaCycles();

		ServletFileUpload fileUpload = new ServletFileUpload();

		try {
			FileItemIterator itemIterator = fileUpload.getItemIterator(req);
			List<ExcelFile> excelFiles = new ArrayList<ExcelFile>();
			List<GeneratedFile> generatedFiles = new ArrayList<GeneratedFile>();

			while (itemIterator.hasNext()) {
				FileItemStream itemStream = itemIterator.next();
				InputStream stream = itemStream.openStream();

				if (itemStream.isFormField()) {

					String fieldName, value;

					fieldName = itemStream.getFieldName();

					byte[] bs = new byte[1024];
					ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

					int len;
					while ((len = stream.read(bs, 0, bs.length)) != -1)
						outputStream.write(bs, 0, len);

					outputStream.flush();
					stream.close();

					value = outputStream.toString();

					outputStream.close();

					if (!value.equals("")) {
						if (fieldName.equals("addressLocation")) {
							prop.setProperty("excel2wsdl.soapaddress.location",
									value);
						} else if (fieldName.equals("targetnamespace")) {
							prop.setProperty(
									"excel2wsdl.targetnamespace.urlprefix",
									value);
						}
					}

				} else {

					byte[] bs = new byte[8192];

					ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();

					int len;
					while ((len = stream.read(bs, 0, bs.length)) != -1) {
						arrayOutputStream.write(bs, 0, len);
					}

					Blob blob = new Blob(arrayOutputStream.toByteArray());
					stream.close();
					arrayOutputStream.close();

					ExcelFile excelFile = new ExcelFile();
					ExcelParser excelParser = new ExcelParser();
					// GeneratedFile generatedFile = new GeneratedFile();
					List<Object> list = excelParser
							.getReqAndRespAndServFromExcel(blob,
									itemStream.getName());

					excelFile.setName(itemStream.getName());
					excelFile.setBlob(blob);
					excelFile.setServices((Set<String>) list.get(0));
					excelFile.setRequestMsg((Map<String, Collection<?>>) list
							.get(1));
					excelFile.setResponseMsg((Map<String, Collection<?>>) list
							.get(2));

					excelFiles.add(excelFile);
				}
			}

			// generate wsdl file
			generatedFiles.addAll(new WSDLGenerator(excelFiles, prop)
					.getGeneratedFiles());

			// generate schema file
			AbstractGenerator generator = new SchemaGenerator(excelFiles, prop);
			generatedFiles.addAll(generator.getGeneratedFiles());
			List<String> error = generator.getERRORMSG();
			for (int i = 0, max = error.size(); i < max; i++)
				jsonArray.add(new JsonPrimitive(error.get(i)));

			CommonUtil.getCache().put(req.getSession().getId(),
					new CommonUtil().toZip(generatedFiles));

		} catch (FileUploadException e) {
			LOGGER.warning(e.toString());
		}
		long end = quotaService.getCpuTimeInMegaCycles();

		jsonObject.addProperty(
				"TIME"/* spending time */,
				Double.toString(quotaService.convertMegacyclesToCpuSeconds(end
						- start)));
		jsonObject.addProperty("ID", req.getSession().getId());
		jsonObject.add("ERROR", jsonArray);

		resp.getWriter().append(new Gson().toJson(jsonObject));
	}
}
