/**
 * 
 */
package com.wsdlgenerator.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.xml.namespace.QName;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.memcache.stdimpl.GCacheFactory;
import com.wsdlgenerator.model.GeneratedFile;

/**
 * @author Kevin.C
 */
public class CommonUtil {

	private static Properties prop = MyProperties.getProperties();
	private static final Logger LOGGER = Logger.getLogger(CommonUtil.class
			.getName());
	private static Cache cache;

	static {

		Map<Object, Object> props = new HashMap<Object, Object>();
		props.put(GCacheFactory.EXPIRATION_DELTA, 600);

		try {

			cache = CacheManager.getInstance().getCacheFactory()
					.createCache(props);

		} catch (CacheException e) {
			LOGGER.warning(e.toString());
		}
	}

	public static Cache getCache() {
		return cache;
	}

	public static Blob toZip(List<GeneratedFile> files) {

		ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
		Blob blob;
		ZipOutputStream zos;
		try {

			zos = new ZipOutputStream(arrayOutputStream);
			GeneratedFile file;

			for (int i = 0, max = files.size(); i < max; i++) {

				file = files.get(i);
				zos.putNextEntry(new ZipEntry(file.getName()));
				zos.write(file.getBlob().getBytes());

				zos.closeEntry();
			}

			arrayOutputStream.flush();
			zos.flush();
			arrayOutputStream.close();
			zos.close();

			blob = new Blob(arrayOutputStream.toByteArray());

			return blob;

		} catch (IOException e) {
			LOGGER.warning(e.toString());
		}
		return null;
	}

	public static QName getSchemaSimpleType(String arg0) {

		String namespace = prop.getProperty("excel2wsdl.namespace.xsd");

		String tmp = arg0.trim();

		if (Pattern.matches(".*STRING$", tmp)) // STRING & LIST OF
												// STRING
			return new QName(namespace, "string");
		else if (Pattern.matches("^INTEGER$", tmp)) // INTEGER
			return new QName(namespace, "int");
		else if (Pattern.matches("^DECIMAL$", tmp)) // DECIMAL
			return new QName(namespace, "decimal");
		else {
			return null;
		}
	}
}
