package com.wsdlgenerator.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import com.google.appengine.api.datastore.Blob;
import com.wsdlgenerator.model.RowObject;

public class ExcelParser {

	private static Properties prop = MyProperties.getProperties();
	private static final Logger LOGGER = Logger.getLogger(ExcelParser.class
			.getName());

	private static int COLUMN_KEY = Integer.parseInt(prop
			.getProperty("excel2wsdl.column.key"));
	private static int COLUMN_LEVEL = Integer.parseInt(prop
			.getProperty("excel2wsdl.column.level"));
	private static int COLUMN_TYPE = Integer.parseInt(prop
			.getProperty("excel2wsdl.column.type"));
	private static int COLUMN_ISNECESSARY = Integer.parseInt(prop
			.getProperty("excel2wsdl.column.isNecessary"));

	/**
	 * 
	 * @param {@link InputStream} file
	 * @param String
	 *            fileName
	 * @return List
	 *         <p>
	 *         index:<br>
	 *         0: {@link Set} - Services<br>
	 *         1: {@link Map} - Request Message<br>
	 *         2: {@link Map} - Response Message
	 *         </p>
	 */
	public List<Object> getReqAndRespAndServFromExcel(Blob file, String fileName) {

		List<Object> list = new ArrayList<Object>();
		String fname = fileName.substring(0, fileName.lastIndexOf("."));
		ByteArrayInputStream inputStream = new ByteArrayInputStream(
				file.getBytes());
		try {
			Sheet[] sheets = Workbook.getWorkbook(inputStream).getSheets();
			list.add(getServicesFromExcel(sheets, fname));
			list.add(getReqFromExcel(sheets, fname));
			list.add(getRespFromExcel(sheets, fname));
			inputStream.close();
			return list;
		} catch (BiffException e) {
			LOGGER.warning(e.toString());
		} catch (IOException e) {
			LOGGER.warning(e.toString());
		}

		return null;
	}

	/**
	 * 取得每個服務的傳入訊息
	 * 
	 * @return Request message map key - string, file name.msgName eg.
	 *         UC_DIS_SALESORDER.QRYSALESORDER value - collection
	 */
	private Map<String, Collection<?>> getReqFromExcel(Sheet[] sheets,
			String fileName) {

		// LOGGER.info(String.format("Start to retrieve %s's request message.",
		// fileName));

		Map<String, Collection<?>> requestMsg = new HashMap<String, Collection<?>>();

		// String msgName;
		// Cell tmpCell;
		int firstP, lastP;

		// Get sheet, variable i
		for (int i = 1, maxSheets = sheets.length; i < maxSheets; i++) {

			Sheet sheet = sheets[i];

			try {
				sheet.getCell(prop.getProperty("excel2wsdl.msgName.position"))
						.getContents().trim();
			} catch (ArrayIndexOutOfBoundsException e) {
				continue;
			}

			LOGGER.info(String.format("Parse %s sheet's request",
					sheet.getName()));

			firstP = sheet.findCell(Pattern.compile("P"), 0, 0, 0,
					sheet.getRowView(0).getSize(), false).getRow();
			lastP = sheet.findCell(Pattern.compile("P"), 0, 0, 0,
					sheet.getRowView(0).getSize(), true).getRow();

			/*
			 * 同個回覆訊息的欄位使用List儲存
			 */
			ArrayList<RowObject> rowObjects = new ArrayList<RowObject>();

			// Get row, variable j
			for (int j = firstP; j <= lastP; j++) {

				Cell[] cells = sheet.getRow(j);

				if (cells[0] == null)
					continue;
				try {
					if (!Pattern.compile("^[A-Z]+[A-Z]$")
							.matcher(cells[COLUMN_KEY].getContents().trim())
							.matches())
						continue;
				} catch (ArrayIndexOutOfBoundsException e) {
					continue;
				}

				RowObject rowObject = new RowObject();

				rowObject.setLevel(cells[COLUMN_LEVEL].getContents().trim());

				rowObject.setKey(cells[COLUMN_KEY].getContents().trim());

				rowObject.setType(cells[COLUMN_TYPE].getContents().trim());

				rowObject.setNecessary(cells[COLUMN_ISNECESSARY].getContents()
						.trim());

				rowObjects.add(rowObject);
			}

			// key example, UC_DIS_SALESORDER.QRYSALESORDER
			requestMsg
					.put(new StringBuffer(fileName)
							.append('.')
							.append(sheet
									.getCell(
											prop.getProperty("excel2wsdl.msgName.position"))
									.getContents().trim()).toString(),
							rowObjects);
		}

		return requestMsg;
	}

	/**
	 * 取得所有的回覆訊息
	 * 
	 * @return Response message map key - string, file name.msgName eg.
	 *         UC_DIS_SALESORDER.QRYSALESORDERResponse value - collection
	 * @throws IOException
	 * @throws BiffException
	 */
	private Map<String, Collection<?>> getRespFromExcel(Sheet[] sheets,
			String fileName) {

		// LOGGER.info(String.format("Start to retrieve %s's response message.",
		// fileName));

		Map<String, Collection<?>> responseMsg = new HashMap<String, Collection<?>>();

		int firstR, lastR;

		// sheet
		for (int i = 1, maxSheets = sheets.length; i < maxSheets; i++) {

			Sheet sheet = sheets[i];

			try {
				sheet.getCell(prop.getProperty("excel2wsdl.msgName.position"))
						.getContents().trim();
			} catch (ArrayIndexOutOfBoundsException e) {
				continue;
			}

			LOGGER.info(String.format("Parse %s sheet's response",
					sheet.getName()));

			firstR = sheet.findCell(Pattern.compile("R"), 0, 0, 0,
					sheet.getRowView(0).getSize(), false).getRow();
			lastR = sheet.findCell(Pattern.compile("R"), 0, 0, 0,
					sheet.getRowView(0).getSize(), true).getRow();

			ArrayList<RowObject> rowObjects = new ArrayList<RowObject>();
			RowObject rowObject;

			// row
			for (int j = firstR; j <= lastR; j++) {

				Cell[] cells = sheet.getRow(j);
				// Check key field
				if (cells[0] == null)
					continue;
				try {
					if (!Pattern.compile("^[A-Z]+[A-Z]$")
							.matcher(cells[COLUMN_KEY].getContents().trim())
							.matches())
						continue;
				} catch (ArrayIndexOutOfBoundsException e) {
					continue;
				}

				rowObject = new RowObject();

				rowObject.setLevel(cells[COLUMN_LEVEL].getContents());

				rowObject.setKey(cells[COLUMN_KEY].getContents());

				rowObject.setType(cells[COLUMN_TYPE].getContents());

				rowObject.setNecessary(cells[COLUMN_ISNECESSARY].getContents());

				rowObjects.add(rowObject);

			}
			// key example, UD_DIS_SALESORDER.QRYSALESORDERResponse
			responseMsg
					.put(new StringBuffer(fileName)
							.append(".")
							.append(sheet
									.getCell(
											prop.getProperty("excel2wsdl.msgName.position"))
									.getContents().trim()).append("Response")
							.toString(), rowObjects);
		}

		return responseMsg;
	}

	private Set<String> getServicesFromExcel(Sheet[] sheets, String fileName) {

		Set<String> sn = new HashSet<String>();

		LOGGER.info(String.format("There are %s services in %s.",
				sheets.length, fileName));

		/*
		 * Get service name in each sheet in workbook but except the first
		 * sheet. Service name was set at B2.
		 */
		for (int i = 1, maxSheets = sheets.length; i < maxSheets; i++) {
			try {
				sn.add(sheets[i]
						.getCell(
								prop.getProperty("excel2wsdl.msgName.position"))
						.getContents().trim());
			} catch (ArrayIndexOutOfBoundsException e) {
				continue;
			}
		}

		return sn;
	}
}
