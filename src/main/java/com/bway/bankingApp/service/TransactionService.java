package com.bway.bankingApp.service;

import java.util.List;

import com.bway.bankingApp.model.Transaction;

public interface TransactionService {
	
	void addTransaction(Transaction transaction);
	
	void deleteTransaction(int id);
	
	Transaction getTransactionById(int id);
	
	List<Transaction> getAllTransactions();
	
	void performTransaction(String accountnumber, String receipentaccountnumber, Long amount);
	
	List<Transaction> getAllTransactionByAccNum(String accountnumber);
	
	List<Transaction> getAllTransactionToAccNum(String accountnumber);

	
}
