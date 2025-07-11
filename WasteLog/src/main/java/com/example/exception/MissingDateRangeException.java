package com.example.exception;

public class MissingDateRangeException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MissingDateRangeException(String message) {
        super(message);
    }
}