package com.bway.bankingApp.service;

import com.bway.bankingApp.model.Admin;

public interface AdminService {
	
	void addAdmin(Admin admin);
	
	void deleteAdmin(int id);
	
	Admin findById(int id);
	
	Admin findByUsernameAndPassword(String username, String password);

	Admin findByUsername(String username);
	
	void changePassword(String username, String newPassword);
	
}
