package org.bountybb.utils;

import java.io.Serializable;

/***
 * Essentially just wraps an array of bytes, allowing the bytes to become serializable
 * this in turn allows the bytes to be sent and received between peers
 * 
 * @author Michael Zemsky
 *  
 * 
 */
public class DataWrapper implements Serializable {

	private static final long serialVersionUID = 2007612126394688791L;
	private final byte[] bytes;
	public DataWrapper(byte[] bytes) {
		this.bytes = bytes;
	}
	
	public byte[] getBytes() {
		return this.bytes;
	}
}
