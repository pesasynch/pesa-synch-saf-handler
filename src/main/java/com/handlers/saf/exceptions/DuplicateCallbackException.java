package com.handlers.saf.exceptions;

public class DuplicateCallbackException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public DuplicateCallbackException(String message) {
        super(message);
    }

    public DuplicateCallbackException(String message, Throwable cause) {
        super(message, cause);
    }

}
