/**
 * 
 */
package com.wsdlgenerator.model;

import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * @author korprulu
 * 
 * message的每個element都是一個rowObject
 */
public class RowObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int level;
	private String key;
	private String type;
	private boolean isNecessary;
	
	public RowObject() {}
	
	public RowObject(int level) {
		this.level = level;
		this.key = "";
		this.type = "";
	}
		
	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}
	
	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}
	
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * @return the isNecessary
	 */
	public boolean isNecessary() {
		return isNecessary;
	}
	
	/**
	 * @param level the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}
	
	/**
	 * 
	 * @param level
	 */
	public void setLevel(String level) {
		this.level = Integer.parseInt(level);
	}
	
	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}
	
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * @param isNecessary the isNecessary to set
	 */
	public void setNecessary(boolean isNecessary) {
		this.isNecessary = isNecessary;
	}
	
	/**
	 * 
	 * @param isNecessary
	 */
	public void setNecessary(String isNecessary) {
		
		if(Pattern.matches("V", isNecessary.trim()))
			this.isNecessary = true;
		else
			this.isNecessary = false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new StringBuffer(level).append(",")
									  .append(key)
									  .append(",")
									  .append(type)
									  .append(",")
									  .append(isNecessary)
									  .toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isNecessary ? 1231 : 1237);
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + level;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		if (!(obj instanceof RowObject))
			return false;
		RowObject other = (RowObject) obj;
		if (isNecessary != other.isNecessary)
			return false;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (level != other.level)
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

}
