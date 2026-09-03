package com.ricardo.bankchallenge2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ricardo.bankchallenge2.entities.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long>{

}
