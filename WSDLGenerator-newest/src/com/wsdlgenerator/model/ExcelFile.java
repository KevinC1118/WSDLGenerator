package com.wsdlgenerator.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.google.appengine.api.datastore.Blob;

/**
 * 
 * @author Kevin.C
 *
 */
public class ExcelFile implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private Blob blob;
	private Set<String> services;
	private Map<String, Collection<?>> requestMsg;
	private Map<String, Collection<?>> responseMsg;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the inputStream
	 */
	public Blob getBlob() {
		return blob;
	}
	/**
	 * @return the requestMsg
	 */
	public Map<String, Collection<?>> getRequestMsg() {
		return requestMsg;
	}
	/**
	 * @return the responseMsg
	 */
	public Map<String, Collection<?>> getResponseMsg() {
		return responseMsg;
	}
	/**
	 * @return the services
	 */
	public Set<String> getServices() {
		return services;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		if(name.indexOf(".xls") == -1)
			this.name = name;
		else
			this.name = name.substring(0, name.indexOf(".xls"));
	}
	/**
	 * @param inputStream the inputStream to set
	 */
	public void setBlob(Blob blob) {
		this.blob = blob;
	}
	/**
	 * @param requestMsg the requestMsg to set
	 */
	public void setRequestMsg(Map<String, Collection<?>> requestMsg) {
		this.requestMsg = requestMsg;
	}
	/**
	 * @param responseMsg the responseMsg to set
	 */
	public void setResponseMsg(Map<String, Collection<?>> responseMsg) {
		this.responseMsg = responseMsg;
	}
	/**
	 * @param services the services to set
	 */
	public void setServices(Set<String> services) {
		this.services = services;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ExcelFile))
			return false;
		ExcelFile other = (ExcelFile) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
}
