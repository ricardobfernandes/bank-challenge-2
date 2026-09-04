package com.ricardo.bankchallenge2.services.exceptions;

public class InvalidAddressException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidAddressException(String message) {
		super(message);
	}
}
