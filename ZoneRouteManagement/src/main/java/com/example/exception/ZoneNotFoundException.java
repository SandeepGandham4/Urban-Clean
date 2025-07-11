package com.example.exception;
public class ZoneNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

    public ZoneNotFoundException(String message) {
        super(message);
    }
}
