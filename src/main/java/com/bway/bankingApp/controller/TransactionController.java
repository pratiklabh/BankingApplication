package com.bway.bankingApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.bway.bankingApp.model.Deposit;
import com.bway.bankingApp.model.Transaction;
import com.bway.bankingApp.model.User;
import com.bway.bankingApp.model.Withdraw;
import com.bway.bankingApp.service.DepositService;
import com.bway.bankingApp.service.TransactionService;
import com.bway.bankingApp.service.UserService;
import com.bway.bankingApp.service.WithdrawService;

import jakarta.servlet.http.HttpSession;

@Controller
public class TransactionController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private DepositService depositService;
	
	@Autowired
	private WithdrawService withdrawService;
	
	
	@GetMapping("/performTransaction")
	public String getPerformTransaction(HttpSession session, Model model) {

		if (session.getAttribute("validUser") == null) {

			return "UserLoginForm";

		}
		String name = (String) session.getAttribute("name");
		String accountnumber = (String) session.getAttribute("accountnumber");
		
		model.addAttribute("name", name);
		model.addAttribute("accountnumber", accountnumber);
		return "PerformTransactionForm";
	}
	
	@PostMapping("/performTransaction")
	public String postPerformTransaction(@ModelAttribute Transaction transaction, Model model, HttpSession session) {
	    
	    String name = (String) session.getAttribute("name");
	    transaction.setName(name);
	    
	    String accountNumber = (String) session.getAttribute("accountnumber");
	    transaction.setAccountnumber(accountNumber);
	    
	    // Ensure recipient account number and name are provided
	    if (transaction.getRecipientaccountnumber() == null || transaction.getRecipientname() == null) {
	        model.addAttribute("error", "Recipient's Account Number and Name must be provided.");
	        return "PerformTransactionForm";
	    }
	    
	    // Check if recipient account exists
	    User recipient = userService.findByAccountnumber(transaction.getRecipientaccountnumber());
	    if (recipient == null) {
	        model.addAttribute("error", "No account found with the provided account number.");
	        return "PerformTransactionForm";
	    }
	    
	    // Ensure the name matches
	    if (!recipient.getName().equalsIgnoreCase(transaction.getRecipientname())) {
	        model.addAttribute("error", "The provided name does not match the account holder's name.");
	        return "ViewTransactionHistory";
	    }
	    
	    // Verify user PIN
	    String userPin = (String) session.getAttribute("pin");
	    String hashedInputPin = DigestUtils.md5DigestAsHex(transaction.getPin().getBytes());

	    if (!hashedInputPin.equals(userPin)) {
	        model.addAttribute("error", "Invalid User PIN.");
	        return "PerformTransactionForm";
	    }
	    
	    // Perform the transaction and record it
	    transactionService.performTransaction(transaction.getAccountnumber(), transaction.getRecipientaccountnumber(), transaction.getAmount());
	    model.addAttribute("message", "Transaction Successful.");
	    transactionService.addTransaction(transaction);
	    
	    return "PerformTransactionForm";
	}

	
	@GetMapping("/viewTransactionHistory")
	public String getTransactionHistory(Model model,HttpSession session) {
		
		if(session.getAttribute("validUser") ==null){
			return "UserLoginForm";
		}
		
		String accountnumber = (String) session.getAttribute("accountnumber");
		
		List<Deposit> deposits = depositService.getDepositsByAccNum(accountnumber);
		List<Withdraw> withdraws = withdrawService.getWithdrawByAccNum(accountnumber);
		List<Transaction> sentTransactions = transactionService.getAllTransactionByAccNum(accountnumber);
		List<Transaction> receivedTransactions = transactionService.getAllTransactionToAccNum(accountnumber);
		
		model.addAttribute("dList", deposits);
		model.addAttribute("wList", withdraws);
		model.addAttribute("sList", sentTransactions);
		model.addAttribute("rList", receivedTransactions);
		
		return "ViewTransactionHistory";
	}
	
}
