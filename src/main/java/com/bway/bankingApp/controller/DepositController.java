package com.bway.bankingApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.bway.bankingApp.model.Admin;
import com.bway.bankingApp.model.Deposit;
import com.bway.bankingApp.model.User;
import com.bway.bankingApp.service.AdminService;
import com.bway.bankingApp.service.DepositService;
import com.bway.bankingApp.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class DepositController {
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private DepositService depoService;

	//to perform deposit
	@GetMapping("/deposit")
	public String getDeposit(HttpSession session) {
		if(session.getAttribute("validAdmin") == null) {
			return "AdminLoginForm";
		}
		return "DepositForm";
	}
	
	@PostMapping("/deposit")
	public String postDeposit(@ModelAttribute Deposit deposit, Model model) {
		deposit.setPassword(DigestUtils.md5DigestAsHex(deposit.getPassword().getBytes()));	
	    if (deposit.getRecipientaccountnumber() == null || deposit.getName() == null) {
	        model.addAttribute("error", "Account Number and Name must be provided.");
	        return "DepositForm";
	    }
	    
	    // to get the user of the provided account number
	    User recipient = userService.findByAccountnumber(deposit.getRecipientaccountnumber());
	    if (recipient == null) {
	        model.addAttribute("error", "No account found with the provided account number.");
	        return "DepositForm";
	    }

	    // comparing the name in the deposit details with the name on the account
	    if (!recipient.getName().equalsIgnoreCase(deposit.getName())) {
	        model.addAttribute("error", "The provided name does not match the account holder's name.");
	        return "DepositForm";
	    }
	    
	    // Verify the admin password
	    Admin admin = adminService.findById(1);
	    String adminPW = admin.getPassword();
	    
	    if (!(deposit.getPassword().equals(adminPW))) {
	        model.addAttribute("error", "Invalid Admin password.");
	        return "DepositForm";
	    }

	    // If all checks pass, perform the deposit
	    
	    model.addAttribute("adminPassword", adminPW);
	    model.addAttribute("DepoPassword", deposit.getPassword());
	    
	    userService.deposit(deposit.getRecipientaccountnumber(), deposit.getAmount());
	    model.addAttribute("message", "Deposit Successful.");
	    depoService.addDeposit(deposit);
	    return "DepositForm";
	}

	
}
