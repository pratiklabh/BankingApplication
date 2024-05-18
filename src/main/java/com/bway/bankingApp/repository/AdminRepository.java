package com.bway.bankingApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bway.bankingApp.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Integer>{
	
	Admin findByUsernameAndPassword(String username, String password);

	Admin findByUsername(String username);
	
	
}
