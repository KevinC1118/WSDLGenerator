/**
 * 
 */
package com.wsdlgenerator.model;

import com.google.appengine.api.datastore.Blob;

/**
 * @author Kevin.C
 *
 */
public class GeneratedFile {
	
	private Blob blob;
	private String name;
	
	/**
	 * @return the file
	 */
	public Blob getBlob() {
		return blob;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param file the file to set
	 */
	public void setBlob(Blob blob) {
		this.blob = blob;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
		if (!(obj instanceof GeneratedFile))
			return false;
		GeneratedFile other = (GeneratedFile) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
