package com.example.workermanagement.exception;


public class WorkerNotFoundException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WorkerNotFoundException(String message) {
        super(message);
    }
}