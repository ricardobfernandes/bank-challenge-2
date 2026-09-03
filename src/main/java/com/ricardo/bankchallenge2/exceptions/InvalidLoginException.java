package com.ricardo.bankchallenge2.exceptions;

public class InvalidLoginException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public InvalidLoginException(String message) {
		super(message);
	}
}
