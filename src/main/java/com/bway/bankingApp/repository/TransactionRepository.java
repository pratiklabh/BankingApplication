package com.bway.bankingApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bway.bankingApp.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Integer>{

	List<Transaction> findTransactionByAccountnumber(String accountnumber);
	
	List<Transaction> findTransactionByRecipientaccountnumber(String recipientaccountnumber);

	
}
