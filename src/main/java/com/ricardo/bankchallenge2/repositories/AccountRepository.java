package com.ricardo.bankchallenge2.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ricardo.bankchallenge2.entities.Account;

public interface AccountRepository extends JpaRepository<Account, Long>{
	
	Optional<Account> findByAgencyNumberAndAccountNumber(
	        Integer agencyNumber,
	        Integer accountNumber);

}
