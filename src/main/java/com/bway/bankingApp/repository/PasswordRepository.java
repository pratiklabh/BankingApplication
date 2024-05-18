package com.bway.bankingApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bway.bankingApp.model.Password;

public interface PasswordRepository extends JpaRepository<Password, Integer> {

	
	
}
