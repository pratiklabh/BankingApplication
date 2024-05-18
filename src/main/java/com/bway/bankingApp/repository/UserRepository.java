package com.bway.bankingApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bway.bankingApp.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	User findByUsernameAndPassword(String username, String password);
	
	User findByAccountnumber(String accountnumber);
	
	User findByName(String name);
	
	User findByUsername(String username);
	
	boolean existsByUsername(String username);
	
	boolean existsByAccountnumber(String accountnumber);
	
	User findByEmail(String email);
}
