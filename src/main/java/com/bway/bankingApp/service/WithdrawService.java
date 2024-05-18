package com.bway.bankingApp.service;

import java.util.List;

import com.bway.bankingApp.model.Withdraw;

public interface WithdrawService {
	
	void addWithdraw(Withdraw withdraw);
	
	Withdraw getWithdrawById(int id);
	
	List<Withdraw> getWithdrawByAccNum(String accountnumber);
		
	List<Withdraw> getAllWithdraws();

}
