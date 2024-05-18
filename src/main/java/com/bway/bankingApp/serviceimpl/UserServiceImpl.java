package com.bway.bankingApp.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bway.bankingApp.model.User;
import com.bway.bankingApp.repository.UserRepository;
import com.bway.bankingApp.service.UserService;

@Service
public class UserServiceImpl implements UserService{

	
	@Autowired
	private UserRepository userRepo;
	
	
	@Override
	public void addUser(User user) {
		userRepo.save(user);
	}

	@Override
	public void deleteUser(int id) {
		userRepo.deleteById(id);
	}

	@Override
	public void updateUser(User user) {

		userRepo.save(user);
		
	}

	@Override
	public User getUserById(int id) {
		return userRepo.findById(id).get();
	}

	@Override
	public List<User> getAllUser() {
		return userRepo.findAll();
	}

	@Override
	public User searchUser(String username, String password) {
		return userRepo.findByUsernameAndPassword(username, password);
	}

	@Override
	public boolean usernameExists(String username) {
		return userRepo.existsByUsername(username);
	}

	@Override
	public boolean accountNumberExists(String accountnumber) {
		return userRepo.existsByAccountnumber(accountnumber);
	}

	@Override
	public User searchAccountnumber(String accountnumber) {
		return userRepo.findByAccountnumber(accountnumber);
	}

	@Override
	public void deposit(String accountnumber, long amount) {

		User user = userRepo.findByAccountnumber(accountnumber);
		
		if(user!=null && amount>0) {
			user.setBalance(user.getBalance()+amount);
			userRepo.save(user);
		}
		
		
	}

	@Override
	public User searchByName(String name) {
		return userRepo.findByName(name);
	}

	@Override
	public User findByAccountnumber(String accountnumber) {
		return userRepo.findByAccountnumber(accountnumber);
	}

	@Override
	public void withdraw(String accountnumber, long amount) {

		User user = userRepo.findByAccountnumber(accountnumber);

		if (user != null && amount < user.getBalance()) {
			user.setBalance(user.getBalance() - amount);
			userRepo.save(user);
		}
		
	}

	@Override
	public User searchbyUsernname(String username) {
		return userRepo.findByUsername(username);
	}

	@Override
	public User searchbyEmail(String email) {
		return userRepo.findByEmail(email);
	}

}
