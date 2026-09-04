package com.ricardo.bankchallenge2.entities.enums;

import com.ricardo.bankchallenge2.services.exceptions.InvalidAccountTypeException;

public enum AccountType {

	CHECKING_ACCOUNT(1),
	SAVINGS_ACCOUNT(2);

	private int code;
	
	private AccountType(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
	
	public static AccountType valueOf(int code) {
		for (AccountType value : AccountType.values()) {
			if (value.getCode() == code) {
				return value;
			}
		}
		throw new InvalidAccountTypeException("Invalid account type code");
	}
}