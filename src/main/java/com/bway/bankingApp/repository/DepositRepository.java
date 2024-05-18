package com.bway.bankingApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bway.bankingApp.model.Deposit;

public interface DepositRepository extends JpaRepository<Deposit, Integer> {

	List<Deposit> getAllDepositsByRecipientaccountnumber(String accountnumber);
	
}
