package com.hsbc.incident.exception;

public class NotFoundException extends RuntimeException {
	public NotFoundException(Long id) {
        super("ID " + id + " not found.");
    }
}
