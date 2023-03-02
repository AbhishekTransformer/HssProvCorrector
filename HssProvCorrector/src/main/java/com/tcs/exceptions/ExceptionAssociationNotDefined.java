package com.tcs.exceptions;

public class ExceptionAssociationNotDefined extends Exception {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;

	public ExceptionAssociationNotDefined() {
		super();
	}
	
	public ExceptionAssociationNotDefined(String message) {
		super(message);
	}

	public ExceptionAssociationNotDefined(String message, Throwable cause) {
		super(message, cause);
	}

}
