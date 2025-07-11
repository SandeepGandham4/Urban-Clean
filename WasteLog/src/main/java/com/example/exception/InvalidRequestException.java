package com.example.exception;

public class InvalidRequestException extends RuntimeException { /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

// Extend RuntimeException
    public InvalidRequestException(String message) {
        super(message);
    }
}
