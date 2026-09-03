package com.ricardo.bankchallenge2.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ricardo.bankchallenge2.entities.Account;
import com.ricardo.bankchallenge2.exceptions.InvalidAmountException;
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
}
