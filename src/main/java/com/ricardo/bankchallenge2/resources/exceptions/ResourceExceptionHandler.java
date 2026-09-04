package com.ricardo.bankchallenge2.resources.exceptions;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ricardo.bankchallenge2.services.exceptions.AccountNotFoundException;
import com.ricardo.bankchallenge2.services.exceptions.AccountAlreadyExistsException;
import com.ricardo.bankchallenge2.services.exceptions.InsufficientFundsException;
import com.ricardo.bankchallenge2.services.exceptions.InvalidAccountTypeException;
import com.ricardo.bankchallenge2.services.exceptions.InvalidAddressException;
import com.ricardo.bankchallenge2.services.exceptions.InvalidAmountException;
import com.ricardo.bankchallenge2.services.exceptions.InvalidContactInfoException;
import com.ricardo.bankchallenge2.services.exceptions.InvalidLoginException;
import com.ricardo.bankchallenge2.services.exceptions.InvalidTypeableLine;
import com.ricardo.bankchallenge2.services.exceptions.TransferNotAllowedException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ResourceExceptionHandler {
	
	@ExceptionHandler(AccountNotFoundException.class)
	public ResponseEntity<StandardError> accountNotFound(AccountNotFoundException e,HttpServletRequest request) {
	    HttpStatus status = HttpStatus.NOT_FOUND;
	    StandardError err = new StandardError(Instant.now(), status.value(),"Account not found",e.getMessage(),request.getRequestURI());
	    return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler({ AccountAlreadyExistsException.class, InsufficientFundsException.class, InvalidAccountTypeException.class, InvalidAddressException.class, InvalidAmountException.class, InvalidContactInfoException.class, InvalidLoginException.class, InvalidTypeableLine.class, TransferNotAllowedException.class })
	public ResponseEntity<StandardError> businessException(RuntimeException e, HttpServletRequest request) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		StandardError err = new StandardError(Instant.now(), status.value(), "Business exception", e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(status).body(err);
}
}
