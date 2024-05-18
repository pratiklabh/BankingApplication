package com.bway.bankingApp.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bway.bankingApp.model.User;
import com.bway.bankingApp.repository.UserRepository;
import com.bway.bankingApp.service.PinService;
@Service
public class PinServiceImpl implements PinService {

	@Autowired
	private UserRepository userRepo;
	
	@Override
	public void changePin(String username, String newPin) {

		User user = userRepo.findByAccountnumber(username);

		user.setPin(newPin);

		userRepo.save(user);

	}

}
