package com.bway.bankingApp.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bway.bankingApp.model.Deposit;
import com.bway.bankingApp.repository.DepositRepository;
import com.bway.bankingApp.service.DepositService;

@Service
public class DepositServiceImpl  implements DepositService{

	
	@Autowired
	private DepositRepository depositRepo;

	@Override
	public void addDeposit(Deposit deposit) {

		depositRepo.save(deposit);
		
	}

	@Override
	public Deposit getDepositById(int id) {
		return depositRepo.findById(id).get();
	}

	@Override
	public List<Deposit> getAllDeposits() {
		return depositRepo.findAll();
	}

	@Override
	public List<Deposit> getDepositsByAccNum(String accountnumber) {
		return depositRepo.getAllDepositsByRecipientaccountnumber(accountnumber);
	}

	
	
}
