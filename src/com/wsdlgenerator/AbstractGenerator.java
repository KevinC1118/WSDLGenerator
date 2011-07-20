/**
 * 
 */
package com.wsdlgenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.wsdlgenerator.model.ExcelFile;
import com.wsdlgenerator.model.GeneratedFile;

/**
 * @author Kevin.C
 * 
 */
public abstract class AbstractGenerator {

	private List<ExcelFile> excelFiles;
	private Properties prop;
	private List<String> ERRORMSG = new ArrayList<String>();

	// protected String destination;

	public AbstractGenerator(List<ExcelFile> excelFiles, Properties prop) {
		this.excelFiles = excelFiles;
		this.prop = prop;
	}

	/**
	 * @return the excelFile
	 */
	protected List<ExcelFile> getExcelFiles() {
		return excelFiles;
	}

	protected Properties getProperty() {
		return this.prop;
	}
	
	public List<String> getERRORMSG() {
		return ERRORMSG;
	}

	public abstract List<GeneratedFile> getGeneratedFiles();
}
