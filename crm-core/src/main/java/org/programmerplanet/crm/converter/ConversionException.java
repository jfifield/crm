package org.programmerplanet.crm.converter;

/**
 * @author <a href="mailto:jfifield@programmerplanet.org">Joseph Fifield<a>
 * 
 * Copyright (c) 2007 Joseph Fifield
 */
public class ConversionException extends RuntimeException {

	/**
	 * 
	 */
	public ConversionException() {
	}

	/**
	 * @param message
	 */
	public ConversionException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ConversionException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ConversionException(String message, Throwable cause) {
		super(message, cause);
	}

}
