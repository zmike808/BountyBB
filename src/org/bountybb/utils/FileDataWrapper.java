package org.bountybb.utils;

import java.io.Serializable;

/***
 * Extension of DataWrapper, provides functionality for adding a filename as well
 * 
 * @author Michael Zemsky
 *  
 * 
 */
public class FileDataWrapper extends DataWrapper implements Serializable {

	private static final long serialVersionUID = 2007612126394688791L;
	private final String fileName;
	public FileDataWrapper(String fileName, byte[] bytes) {
		super(bytes);
		this.fileName = fileName;
	}
	
	public String getFileName() {
		return this.fileName;
	}
}
