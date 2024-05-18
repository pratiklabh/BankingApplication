package com.bway.bankingApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bway.bankingApp.model.Admin;
import com.bway.bankingApp.model.Deposit;
import com.bway.bankingApp.model.Password;
import com.bway.bankingApp.model.Transaction;
import com.bway.bankingApp.model.User;
import com.bway.bankingApp.model.Withdraw;
import com.bway.bankingApp.service.AdminService;
import com.bway.bankingApp.service.DepositService;
import com.bway.bankingApp.service.PasswordService;
import com.bway.bankingApp.service.TransactionService;
import com.bway.bankingApp.service.UserService;
import com.bway.bankingApp.service.WithdrawService;

import jakarta.servlet.http.HttpSession;
@Controller
public class AdminController {
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private DepositService depositService;
	
	@Autowired
	private WithdrawService withdrawService;
	
	@Autowired
	private TransactionService transactionService;
		
	
	@GetMapping("/")
	public String login() {
		
		return "Login";
	}
	
	
	//to sign up for admin
	@GetMapping("/adminSignup")
	public String getSignup() {
		
		return "AdminSignupForm";
	}
	
	@PostMapping("/adminSignup")
	public String postSignup(@ModelAttribute Admin admin) {
		
		
		admin.setPassword(DigestUtils.md5DigestAsHex(admin.getPassword().getBytes()));
		
		adminService.addAdmin(admin);
		return "AdminLoginForm";
		
	}
	
	//admin login
	@GetMapping("/adminLogin")
	public String getAdminLogin() {
		
		return "AdminLoginForm";
	}
	
	
	
	@PostMapping("/adminLogin")
	public String postAdminLogin(@ModelAttribute Admin admin,Model model, HttpSession session) {
		admin.setPassword(DigestUtils.md5DigestAsHex(admin.getPassword().getBytes()));
		Admin ad =adminService.findByUsernameAndPassword(admin.getUsername(), admin.getPassword());
		
		if(ad != null) {
			
			session.setAttribute("validAdmin", ad);
			
			session.setAttribute("name", ad.getName());
	        session.setAttribute("email", ad.getEmail());
	        session.setAttribute("username", ad.getUsername());
	        session.setAttribute("phone", ad.getPhone());
			
			session.setMaxInactiveInterval(120);
			
			return "AdminHome";
		}
		
		model.addAttribute("error", "user not found");
		return "AdminLoginForm";
	}
	
	
	//to add new user
	@GetMapping("/userSignupForm")
	public String getAddUser(HttpSession session) {
		
		if(session.getAttribute("validAdmin") == null) {
			return "AdminLoginForm";
		}
		
		return "UserSignupForm";
	}
	
	@PostMapping("/userSignupForm")
	public String postAddUser(@ModelAttribute User user, Model model) {
	    
	    // checking the length of the PIN
	    String pin = user.getPin();
	    if (pin == null || pin.length() != 4) {
	        model.addAttribute("error", "Pin should be 4 digits");
	        return "UserSignupForm";
	    }
	    
	    // After confirming the PIN length, hashing the password and PIN
	    user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
	    user.setPin(DigestUtils.md5DigestAsHex(pin.getBytes())); 
	    
	    // Checking if username exists
	    if (userService.usernameExists(user.getUsername())) {
	        model.addAttribute("error", "Username Already Exists");
	        return "UserSignupForm";
	    }
	    
	    // Checking if account number exists
	    if (userService.accountNumberExists(user.getAccountnumber())) {
	        model.addAttribute("error", "Account Number Already Exists");
	        return "UserSignupForm";
	    }
	    
	    // Add the user after all validations 
	    userService.addUser(user);
	    model.addAttribute("message", "User Added Successfully");
	    return "userSignupForm";
	}

	//to view User list
	@GetMapping("/userList")
	public String getUserList(Model model, HttpSession session) {
		
		if(session.getAttribute("validAdmin") == null) {
			return "AdminLoginForm";
		}
		
		model.addAttribute("uList", userService.getAllUser());
		return "UserListForm";
	}
	
	@PostMapping("/userList")
	public String postUserList(Model model) {
		model.addAttribute("uList", userService.getAllUser());
		return "UserListForm";
	}
	
	//to delete 
	@GetMapping("/user/delete")
	public String deleteUser(@RequestParam int id, HttpSession session) {
		
		if(session.getAttribute("validAdmin") == null) {
			return "AdminLoginForm";
		}
		
		userService.deleteUser(id);
		return "redirect:/UserList";
	}
	
	//to edit
	@GetMapping("/user/edit")
	public String editUser(@RequestParam int id ,Model model, HttpSession session) {
		
		if(session.getAttribute("validAdmin") == null) {
			return "AdminLoginForm";
		}
		
		model.addAttribute("uModel", userService.getUserById(id));
		return "UserEditForm";
		
	}
	
	//to update
	@PostMapping("/user/update")
	public String updateUser(@ModelAttribute User user) {
		
		userService.updateUser(user);
		return "redirect:/userList";
	}
	
	
	//to view details
	@GetMapping("user/viewDetails")
	public String viewUserDetails(@RequestParam int id, Model model, HttpSession session) {
		
		if(session.getAttribute("validAdmin") == null) {
			return "AdminLoginForm";
		}
		
		model.addAttribute("uModel", userService.getUserById(id));
		return "UserDetailsForm";
	}
	
	//to view profile 
	@GetMapping("/profile")
	public String profile(Model model,HttpSession session) {
		
		if(session.getAttribute("validAdmin") == null) {
			return "AdminLoginForm";
		}
				
		String name = (String) session.getAttribute("name");
        String email = (String) session.getAttribute("email");
        String username = (String) session.getAttribute("username");
        String phone = (String) session.getAttribute("phone");
        
        model.addAttribute("name", name);
        model.addAttribute("email", email);
        model.addAttribute("username", username);
        model.addAttribute("phone", phone);
        
		
		return "AdminProfileForm";
	}
	
	
	//to view transaction details
	@GetMapping("/history")
	public String getHistory(Model model,HttpSession session) {
		
		if(session.getAttribute("validAdmin") ==null){
			return "AdminLoginForm";
		}
		
		model.addAttribute("userList", userService.getAllUser());
		return "History";
	}
	
	
	@PostMapping("/history")
	public String getTransactionHistory(@RequestParam String accountnumber,Model model,HttpSession session) {
		
		if(session.getAttribute("validAdmin") ==null){
			return "AdminLoginForm";
		}
		
		
		List<Deposit> deposits = depositService.getDepositsByAccNum(accountnumber);
		List<Withdraw> withdraws = withdrawService.getWithdrawByAccNum(accountnumber);
		List<Transaction> sentTransactions = transactionService.getAllTransactionByAccNum(accountnumber);
		List<Transaction> receivedTransactions = transactionService.getAllTransactionToAccNum(accountnumber);
		
		model.addAttribute("dList", deposits);
		model.addAttribute("wList", withdraws);
		model.addAttribute("sList", sentTransactions);
		model.addAttribute("rList", receivedTransactions);
		
		return "AccountHistory";
	}
	
	
	
	
	
	
	
	@GetMapping("/changeAdminPassword")
	public String getChangePassword(HttpSession session) {

		if(session.getAttribute("validAdmin") ==null){
			return "AdminLoginForm";
		}
		
		return "ChangeAdminPassword";
	}

	@PostMapping("/changeAdminPassword")
	public String postChangePassword(@ModelAttribute Password password, Model model, HttpSession session) {

		String username = (String) session.getAttribute("username");
		model.addAttribute("un", username);
		Admin admin = adminService.findByUsername(username);

		password.setOldPassword(DigestUtils.md5DigestAsHex(password.getOldPassword().getBytes()));
		password.setNewPassword(DigestUtils.md5DigestAsHex(password.getNewPassword().getBytes()));
		password.setConfirmNewPassword(DigestUtils.md5DigestAsHex(password.getConfirmNewPassword().getBytes()));

		if (admin == null) {
	        model.addAttribute("error", "No admin data found.");
	        return "ChangeAdminPassword"; 
	    }
		
		if (!admin.getPassword().equals(password.getOldPassword())) {

			model.addAttribute("error", "You entered wrong password!!!");
			return "ChangeAdminPassword";

		}
		
		if(!password.getNewPassword().equals(password.getConfirmNewPassword())) {
			
			model.addAttribute("error", "The new passwords do not match!!!");
			return "ChangeAdminPassword";
		}
		
		if(admin.getPassword().equals(password.getNewPassword())) {
			model.addAttribute("error", "Old password and New passwords cannot be same !!!");
			return "ChangeAdminPassword";
		}
		
		adminService.changePassword(username, password.getNewPassword());
		model.addAttribute("message", "Your password has been changed!!!");
		return "ChangeAdminPassword";
	}
	
	//to logout
	@PostMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "Login";
	}
	
}
