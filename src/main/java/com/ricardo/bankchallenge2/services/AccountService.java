package com.ricardo.bankchallenge2.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ricardo.bankchallenge2.entities.Account;
import com.ricardo.bankchallenge2.entities.enums.AccountType;
import com.ricardo.bankchallenge2.exceptions.InsufficientFundsException;
import com.ricardo.bankchallenge2.exceptions.InvalidAccountTypeException;
import com.ricardo.bankchallenge2.exceptions.InvalidAddressException;
import com.ricardo.bankchallenge2.exceptions.InvalidAmountException;
import com.ricardo.bankchallenge2.exceptions.InvalidContactInfoException;
import com.ricardo.bankchallenge2.exceptions.InvalidLoginException;
import com.ricardo.bankchallenge2.repositories.AccountRepository;

@Service
public class AccountService {

	@Autowired
	private AccountRepository repository;

	public List<Account> findAll() {
		return repository.findAll();
	}

	public Account findById(Long id) {
		return repository.findById(id).get();
	}

	public Account insert(Account account) {
		return repository.save(account);
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}
	
	public void deposit(Long id, Double amount) {
		Account account = findById(id);
		if (amount <= 0) {
		    throw new InvalidAmountException("Deposit amount must be greater than zero.");
		}
		account.deposit(amount);
		repository.save(account);
	}
	
	public void withdraw(Long id, Double amount) {
		Account account = findById(id);
		if (amount <= 0) {
		    throw new InvalidAmountException("Withdrawal amount must be greater than zero.");
		}
		if (account.getAccountType() == AccountType.CHECKING_ACCOUNT && amount > account.getBalance() + account.getCreditLimit()) {
		    throw new InsufficientFundsException("Insufficient funds!");
		}
		if (account.getAccountType() == AccountType.SAVINGS_ACCOUNT && amount > account.getBalance()) {
		    throw new InsufficientFundsException("Insufficient funds!");
		}
		account.withdraw(amount);
		repository.save(account);
	}
	
	public Double checkBalance(Long id) {
	    Account account = findById(id);
	    return account.getBalance();
	}
	
	public Double checkLimit(Long id) {
	    Account account = findById(id);
	    if (account.getAccountType() == AccountType.SAVINGS_ACCOUNT) {
	        throw new InvalidAccountTypeException("This account does not have credit limit.");
	    }
	    return account.getCreditLimit();
	}
	
	public void requestLimit(Long id, Double amount) {
	    Account account = findById(id);
	    if (account.getAccountType() == AccountType.SAVINGS_ACCOUNT) {
	        throw new InvalidAccountTypeException("This is not a Checking Account!");
	    }
	    if (amount <= 0) {
	        throw new InvalidAmountException("Limit increase must be greater than zero.");
	    }
	    account.increaseCreditLimit(amount);
	    repository.save(account);
	}
	
	public void changePassword(Long id, String currentPassword, String newPassword) {
		Account account = findById(id);
		if (!account.getPassword().equals(currentPassword)) {
			throw new InvalidLoginException("Current password is incorrect.");
		}
		if (newPassword.isBlank()) {
			throw new InvalidLoginException("Password must not be empty.");
		}
		account.setPassword(newPassword);
		repository.save(account);
	}
	
	public void changeAddress(Long id, String newAddress) {
	    Account account = findById(id);
	    if (newAddress.isBlank()) {
	        throw new InvalidAddressException("Your Address must not be empty or blank");
	    }
	    account.setAddress(newAddress);
	    repository.save(account);
	}
	
	public void changeContactInformation(Long id, String newPhoneNumber, String newEmail) {
	    Account account = findById(id);
	    if (newPhoneNumber.isBlank() || newPhoneNumber.length() < 10) {
	        throw new InvalidContactInfoException("Phone number must not be empty or blank");
	    }
	    if (newEmail.isBlank() || !newEmail.contains("@")) {
	        throw new InvalidContactInfoException("Invalid email address.");
	    }
	    account.setPhoneNumber(newPhoneNumber);
	    account.setEmail(newEmail);
	    repository.save(account);
	}
	
	public void blockCard(Long id) {
	    Account account = findById(id);
	    if (account.isCardBlocked()) {
	        return;
	    }
	    account.blockCard();
	    repository.save(account);
	}
	
	public void requestLoan(Long id, Double loanAmount) {
	    Account account = findById(id);
	    if (account.getAccountType() == AccountType.SAVINGS_ACCOUNT) {
	        throw new InvalidAccountTypeException("This is not a Checking Account!");
	    }
	    account.deposit(loanAmount);
	    repository.save(account);
	}
}
