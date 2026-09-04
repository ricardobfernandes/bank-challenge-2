package com.ricardo.bankchallenge2.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import com.ricardo.bankchallenge2.entities.Account;
import com.ricardo.bankchallenge2.entities.enums.AccountType;
import com.ricardo.bankchallenge2.repositories.AccountRepository;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Override
	public void run(String... args) throws Exception {

	    Account a1 = new Account(null, 1, 11111,"123456", "Rua Alfa 1", "31911111111", "joaocorrente@teste.com", AccountType.CHECKING_ACCOUNT, "Joao Silva", 1000.0);
	    a1.setCreditLimit(500.0);
	    Account a2 = new Account(null, 1, 22222, "123456", "Rua Beta 2", "31922222222", "mariapoupanca@teste.com", AccountType.SAVINGS_ACCOUNT, "Maria Souza", 2500.0);
	    a2.setInterestRate(0.005);
	    accountRepository.saveAll(Arrays.asList(a1, a2));
	}

}
