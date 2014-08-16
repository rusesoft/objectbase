package org.objectbase;

public class OBException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8279947968736204863L;
	
	public OBException(String message) {
		super(message);
	}
	
	public OBException(String message, Throwable t) {
		super(message, t);
	}
}
