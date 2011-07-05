/**
 * 
 */
package com.wsdlgenerator.util;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author Kevin
 * 
 */
public class MyProperties extends Properties {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8963179613043872783L;
	private static final Logger LOGGER = Logger.getLogger(MyProperties.class
			.getName());

	/**
	 * 
	 */
	public MyProperties() {
		ClassLoader loader = MyProperties.class.getClassLoader();
		if (loader == null)
			loader = ClassLoader.getSystemClassLoader();

		String filePath = "excel2wsdl.properties";
		URL url = loader.getResource(filePath);

		try {
			this.load(url.openStream());
		} catch (IOException e) {
			LOGGER.warning(e.toString());
		}
	}

	public static Properties getProperties() {
		return new MyProperties();
	}

}
