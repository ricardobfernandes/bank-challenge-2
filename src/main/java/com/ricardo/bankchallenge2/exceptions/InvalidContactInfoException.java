package com.ricardo.bankchallenge2.exceptions;

public class InvalidContactInfoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidContactInfoException(String message) {
		super(message);
	}
}
