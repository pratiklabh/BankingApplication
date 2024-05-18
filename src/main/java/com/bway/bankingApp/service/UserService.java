package com.bway.bankingApp.service;

import java.util.List;

import com.bway.bankingApp.model.User;

public interface UserService {
	
	void addUser(User user);

	void deleteUser(int id);
	
	void updateUser(User user);
	
	User getUserById(int id);
	
	List<User> getAllUser();
	
	User searchUser(String username, String password);
	
	User searchByName(String name);
	
	User findByAccountnumber(String accountnumber);
	
	boolean usernameExists(String username);
	
	boolean accountNumberExists(String accountnumber);
	
	User searchAccountnumber(String accountnumber);
	
	void deposit(String accountnumber,long amount);
	
	void withdraw (String accountnumber,long amount);
	
	User searchbyUsernname(String username);
	
	User searchbyEmail(String email);
	
}
