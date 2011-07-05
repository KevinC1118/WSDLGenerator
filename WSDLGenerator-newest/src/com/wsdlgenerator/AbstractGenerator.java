/**
 * 
 */
package com.wsdlgenerator;

import java.util.List;

import com.wsdlgenerator.model.ExcelFile;
import com.wsdlgenerator.model.GeneratedFile;



/**
 * @author Kevin.C
 *
 */
public abstract class AbstractGenerator {

	private List<ExcelFile> excelFiles;
//	protected String destination;
	
	public AbstractGenerator(List<ExcelFile> excelFiles) {
		this.excelFiles = excelFiles;
	}

	/**
	 * @return the excelFile
	 */
	protected List<ExcelFile> getExcelFiles() {
		return excelFiles;
	}
	
	public static AbstractGenerator getWSDLGenerator(List<ExcelFile> excelFiles) {
		return new WSDLGenerator(excelFiles);
	}
	
	public static AbstractGenerator getSchemaGenerator(List<ExcelFile> excelFiles) {
		return new SchemaGenerator(excelFiles);
	}

	public abstract List<GeneratedFile> getGeneratedFiles();
}
