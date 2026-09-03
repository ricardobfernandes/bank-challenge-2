package com.ricardo.bankchallenge2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ricardo.bankchallenge2.entities.Account;

public interface AccountRepository extends JpaRepository<Account, Long>{

}
