package com.bway.bankingApp.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bway.bankingApp.model.Withdraw;
import com.bway.bankingApp.repository.WithdrawRepository;
import com.bway.bankingApp.service.WithdrawService;

@Service
public class WithdrawServiceImpl implements WithdrawService{

	@Autowired
	private WithdrawRepository withdrawRepo;
	
	@Override
	public void addWithdraw(Withdraw withdraw) {
		withdrawRepo.save(withdraw);
	}

	@Override
	public Withdraw getWithdrawById(int id) {
		return withdrawRepo.findById(id).get();
	}

	@Override
	public List<Withdraw> getAllWithdraws() {
		return withdrawRepo.findAll();
	}

	@Override
	public List<Withdraw> getWithdrawByAccNum(String accountnumber) {
		return withdrawRepo.findWithdrawsByWithdrawaccountnumber(accountnumber);
	}

}
