package com.bway.bankingApp.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bway.bankingApp.model.Admin;
import com.bway.bankingApp.repository.AdminRepository;
import com.bway.bankingApp.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService{

	@Autowired
	private AdminRepository adminRepo;
	
	
	@Override
	public void addAdmin(Admin admin) {
		adminRepo.save(admin);
	}

	@Override
	public void deleteAdmin(int id) {
		adminRepo.deleteById(id);
	}

	@Override
	public Admin findByUsernameAndPassword(String username, String password) {
		
		return adminRepo.findByUsernameAndPassword(username, password);
	}

	@Override
	public Admin findById(int id) {
		return adminRepo.findById(id).get();
	}

	@Override
	public Admin findByUsername(String username) {
		return adminRepo.findByUsername(username);
	}

	@Override
	public void changePassword(String username, String newPassword) {
		
		Admin admin = adminRepo.findByUsername(username);
		admin.setPassword(newPassword);
		
		adminRepo.save(admin);
		
	}


}
