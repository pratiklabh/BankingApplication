package com.bway.bankingApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bway.bankingApp.model.Withdraw;

public interface WithdrawRepository extends JpaRepository<Withdraw, Integer> {

	List<Withdraw> findWithdrawsByWithdrawaccountnumber(String accountnumber);
	
}
