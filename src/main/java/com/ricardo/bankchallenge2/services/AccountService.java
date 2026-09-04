package com.ricardo.bankchallenge2.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ricardo.bankchallenge2.entities.Account;
import com.ricardo.bankchallenge2.entities.Transaction;
import com.ricardo.bankchallenge2.entities.enums.AccountType;
import com.ricardo.bankchallenge2.repositories.AccountRepository;
import com.ricardo.bankchallenge2.services.exceptions.AccountAlreadyExistsException;
import com.ricardo.bankchallenge2.services.exceptions.AccountNotFoundException;
import com.ricardo.bankchallenge2.services.exceptions.InsufficientFundsException;
import com.ricardo.bankchallenge2.services.exceptions.InvalidAccountTypeException;
import com.ricardo.bankchallenge2.services.exceptions.InvalidAddressException;
import com.ricardo.bankchallenge2.services.exceptions.InvalidAmountException;
import com.ricardo.bankchallenge2.services.exceptions.InvalidContactInfoException;
import com.ricardo.bankchallenge2.services.exceptions.InvalidLoginException;
import com.ricardo.bankchallenge2.services.exceptions.InvalidTypeableLine;
import com.ricardo.bankchallenge2.services.exceptions.TransferNotAllowedException;

@Service
public class AccountService {

	@Autowired
	private AccountRepository repository;

	public List<Account> findAll() {
		return repository.findAll();
	}

	public Account findById(Long id) {
		return repository.findById(id).orElseThrow(() -> new AccountNotFoundException("Account not found!"));
	}

	public Account insert(Account account) {
		Optional<Account> existingAccount = repository.findByAgencyNumberAndAccountNumber(account.getAgencyNumber(),
				account.getAccountNumber());
		if (existingAccount.isPresent()) {
			throw new AccountAlreadyExistsException("Account already exists!");
		}
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
		account.addTransaction("DEPOSIT", amount);
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
		account.addTransaction("WITHDRAW", -amount);
		repository.save(account);
	}
	
	public void transfer(Long sourceId, Integer destinationAgency, Integer destinationAccountNumber, Double amount) {
		Account source = findById(sourceId);
		if (amount <= 0) {
			throw new InvalidAmountException("Transfer amount must be greater than zero.");
		}
		if (source.getAccountType() == AccountType.CHECKING_ACCOUNT && amount > source.getBalance() + source.getCreditLimit()) {
			throw new InsufficientFundsException("Insufficient funds!");
		}
		if (source.getAccountType() == AccountType.SAVINGS_ACCOUNT && amount > source.getBalance()) {
			throw new InsufficientFundsException("Insufficient funds!");
		}
		if (amount > 1000) {
			LocalTime now = LocalTime.now();
			if (now.isBefore(LocalTime.of(6, 0)) || now.isAfter(LocalTime.of(22, 0))) {
				throw new TransferNotAllowedException("Transfers above 1000 are only allowed between 06:00 and 22:00!");
			}
		}
		Account destination = repository.findByAgencyNumberAndAccountNumber(destinationAgency, destinationAccountNumber).orElseThrow(() -> new AccountNotFoundException("Account number not found!"));
		source.withdraw(amount);
		destination.deposit(amount);
		source.addTransaction("TRANSFER_OUT", -amount);
		destination.addTransaction("TRANSFER_IN", amount);
		repository.save(source);
		repository.save(destination);
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
	
	public void requestLoan(Long id, Double loanAmount) {
	    Account account = findById(id);
	    if (account.getAccountType() == AccountType.SAVINGS_ACCOUNT) {
	        throw new InvalidAccountTypeException("This is not a Checking Account!");
	    }
	    account.deposit(loanAmount);
	    account.addTransaction("LOAN_DEPOSIT", loanAmount);
	    repository.save(account);
	}
	
	public void payBill(Long id, String typeableLine) {
		Account account = findById(id);
		if (typeableLine.length() != 47) {
			throw new InvalidTypeableLine("This typeableline is invalid or incomplete!");
		}
		String dueDateString = typeableLine.substring(33, 37);
		int dueDate = Integer.parseInt(dueDateString);
		String amountString = typeableLine.substring(37, 47);
		double amount = Double.parseDouble(amountString) / 100;
		LocalDate baseDate = LocalDate.of(2022, 5, 29);
		LocalDate today = LocalDate.now();
		int currentDate = (int) ChronoUnit.DAYS.between(baseDate, today);
		int difference = currentDate - dueDate;
		if (account.getBalance() >= amount) {
		    if (difference <= 0) {
		        account.withdraw(amount);
		    } else {
		        amount = amount * 1.02 + difference * amount / 100;
		        account.withdraw(amount);
		    }
		} else {
		    throw new InsufficientFundsException("Insufficient funds to execute payment! Please make a deposit!");
		}
		account.addTransaction("BILL_PAYMENT", -amount);
		repository.save(account);
	}
	
	public void investMoney(Long id, Double amount) {
		Account account = findById(id);
		if (account.getAccountType() == AccountType.SAVINGS_ACCOUNT) {
			throw new InvalidAccountTypeException("This investment product is available only for Checking Accounts.");
		}
		if (amount <= 0) {
			throw new InvalidAmountException("Investment amount must be greater than zero.");
		}
		if (account.getBalance() < amount) {
			throw new InsufficientFundsException("Insufficient funds for investment.");
		}
		account.withdraw(amount);
		account.setInvestedBalance(account.getInvestedBalance() + amount);
		account.addTransaction("CDI_INVESTMENT", -amount);
		repository.save(account);
	}
	
	public void blockCard(Long id) {
	    Account account = findById(id);
	    if (account.isCardBlocked()) {
	        throw new InvalidAccountTypeException("Card is already blocked.");
	    }
	    account.blockCard();
	    repository.save(account);
	}
	
	public List<Transaction> getTransactions(Long id) {
	    Account account = findById(id);
	    return account.getTransactions();
	}
	
}
