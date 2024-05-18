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
import com.bway.bankingApp.model.Withdraw;
import com.bway.bankingApp.service.AdminService;
import com.bway.bankingApp.service.DepositService;
import com.bway.bankingApp.service.UserService;
import com.bway.bankingApp.service.WithdrawService;

import jakarta.servlet.http.HttpSession;

@Controller
public class WithdrawController {
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private WithdrawService withdrawService;

	//to perform deposit
	@GetMapping("/withdraw")
	public String getWithdraw(HttpSession session) {
		
		if(session.getAttribute("validAdmin") == null) {
			return "AdminLoginForm";
		}
		
		return "WithdrawForm";
	}
	
	@PostMapping("/withdraw")
	public String postWithdraw(@ModelAttribute Withdraw withdraw, Model model) {
		withdraw.setPassword(DigestUtils.md5DigestAsHex(withdraw.getPassword().getBytes()));
	    
	    if (withdraw.getWithdrawaccountnumber() == null || withdraw.getName() == null) {
	        model.addAttribute("error", "Account Number and Name must be provided.");
	        return "WithdrawForm";
	    }
	    
	    // to get the user of the provided account number
	    User recipient = userService.findByAccountnumber(withdraw.getWithdrawaccountnumber());
	    if (recipient == null) {
	        model.addAttribute("error", "No account found with the provided account number.");
	        return "WithdrawForm";
	    }

	    // comparing the name in the deposit details with the name on the account
	    if (!recipient.getName().equalsIgnoreCase(withdraw.getName())) {
	        model.addAttribute("error", "The provided name does not match the account holder's name.");
	        return "WithdrawForm";
	    }
	    
	    // Verify the admin password
	    Admin admin = adminService.findById(1);
	    String adminPW = admin.getPassword();
	    
	    if (!(withdraw.getPassword().equals(adminPW))) {
	        model.addAttribute("error", "Invalid Admin password.");
	        return "WithdrawForm";
	    }

	    
	    userService.withdraw(withdraw.getWithdrawaccountnumber(), withdraw.getAmount());
	    model.addAttribute("message", "Withdraw Successful.");
	    withdrawService.addWithdraw(withdraw);
	    return "WithdrawForm";
	}

	
}
