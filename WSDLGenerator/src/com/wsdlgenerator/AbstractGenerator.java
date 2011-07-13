/**
 * 
 */
package com.wsdlgenerator;

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

//	public AbstractGenerator getWSDLGenerator(List<ExcelFile> excelFiles,
//			Properties prop) {
//		AbstractGenerator generator = new WSDLGenerator(excelFiles);
//		generator.setProperty(prop);
//		return generator;
//	}
//
//	public AbstractGenerator getSchemaGenerator(List<ExcelFile> excelFiles,
//			Properties prop) {
//		AbstractGenerator generator = new SchemaGenerator(excelFiles);
//		generator.setProperty(prop);
//		return generator;
//	}

	public abstract List<GeneratedFile> getGeneratedFiles();
}
