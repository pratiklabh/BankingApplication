package com.bway.bankingApp.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bway.bankingApp.model.User;
import com.bway.bankingApp.repository.UserRepository;
import com.bway.bankingApp.service.PasswordService;

@Service
public class PasswordServiceImpl implements PasswordService{
	@Autowired
	private UserRepository userRepo;
	
	
	
	@Override
	public void changePassword(String accountnumber, String newPassword) {
		
		User user = userRepo.findByAccountnumber(accountnumber);
		
		user.setPassword(newPassword);
		
		userRepo.save(user);
		
	}

}
