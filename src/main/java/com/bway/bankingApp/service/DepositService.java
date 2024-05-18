package com.bway.bankingApp.service;

import java.util.List;

import com.bway.bankingApp.model.Deposit;

public interface DepositService {
	
	void addDeposit(Deposit deposit);
	
	Deposit getDepositById(int id);
	
	List<Deposit> getDepositsByAccNum(String accountnumber);
	
	List<Deposit> getAllDeposits();

}
