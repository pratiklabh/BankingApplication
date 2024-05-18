package com.bway.bankingApp.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bway.bankingApp.model.Transaction;
import com.bway.bankingApp.model.User;
import com.bway.bankingApp.repository.TransactionRepository;
import com.bway.bankingApp.repository.UserRepository;
import com.bway.bankingApp.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService{

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private TransactionRepository transactionRepo;
	
	@Override
	public void addTransaction(Transaction transaction) {
		transactionRepo.save(transaction);
	}

	@Override
	public void deleteTransaction(int id) {

		transactionRepo.deleteById(id);
	}

	@Override
	public Transaction getTransactionById(int id) {
		return transactionRepo.findById(id).get();
	}

	@Override
	public List<Transaction> getAllTransactions() {
		return transactionRepo.findAll();
	}

	

	@Override
	public void performTransaction(String accountnumber, String receipentaccountnumber, Long amount) {
		
		User senderUser 	= userRepo.findByAccountnumber(accountnumber);
		User receipentUser	= userRepo.findByAccountnumber(receipentaccountnumber);
		
		if(senderUser!=null && amount < senderUser.getBalance()) {
			
			senderUser.setBalance(senderUser.getBalance()-amount);
			receipentUser.setBalance(receipentUser.getBalance() + amount);
			userRepo.save(senderUser);
			userRepo.save(receipentUser);
			
		}
		
	}

	@Override
	public List<Transaction> getAllTransactionByAccNum(String accountnumber) {
		return transactionRepo.findTransactionByAccountnumber(accountnumber);
	}

	@Override
	public List<Transaction> getAllTransactionToAccNum(String accountnumber) {
		return transactionRepo.findTransactionByRecipientaccountnumber(accountnumber);
	}

	
}
