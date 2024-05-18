package com.bway.bankingApp.controller;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bway.bankingApp.model.User;
import com.bway.bankingApp.service.UserService;
import com.bway.bankingApp.utils.MailUtils;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MailUtils mailUtils;

	@GetMapping("/userLogin")
	public String getUserLogin() {
		
		return "UserLoginForm";
	}
	
	@PostMapping("/userLogin")
	public String postUserLogin(@ModelAttribute User user, Model model, HttpSession session) {
		user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));

		User usr = userService.searchUser(user.getUsername(), user.getPassword());
		
		if(usr!=null) {
			
			session.setAttribute("validUser", usr);
			
			session.setAttribute("name", usr.getName());
			session.setAttribute("username", usr.getUsername());
			session.setAttribute("accountnumber", usr.getAccountnumber());
			session.setAttribute("balance", usr.getBalance());
			session.setAttribute("pin", usr.getPin());
			
	        session.setAttribute("phone", usr.getPhone());
	        session.setAttribute("email", usr.getEmail());
	        session.setAttribute("address", usr.getAddress());
	        
	        
	        
			return "UserHome";  
			
		}
		
		
		model.addAttribute("error", "User Not Found!!!");
		return "UserLoginForm";
	}
	
	@GetMapping("/accountDetails")
	public String getAccountDetails(HttpSession session, Model model) {
		
		if(session.getAttribute("validUser")==null) {
			
			return "UserLoginForm";
			
		}
		
		String name = (String) session.getAttribute("name");
		String username = (String) session.getAttribute("username");
		String accountnumber = (String) session.getAttribute("accountnumber");
		Long balance =(Long) session.getAttribute("balance");
		String phone = (String) session.getAttribute("phone");
		String email = (String) session.getAttribute("email");
		String address = (String) session.getAttribute("address");
		
		
		model.addAttribute("name", name);
		model.addAttribute("username", username );
		model.addAttribute("accountnumber", accountnumber);
		model.addAttribute("balance", balance);
		model.addAttribute("phone", phone);
		model.addAttribute("email", email);
		model.addAttribute("address", address);
		
		return "UserAccountDetailsForm";
	}
	
	 @GetMapping("/forgetPassword")
	    public String getForgetPassword() {
	        return "ForgetPasswordForm";
	    }

	    @PostMapping("/forgetPassword")
	    public String postForgetPassword(@RequestParam String toEmail, Model model) {
	        User existingUser = userService.searchbyEmail(toEmail);
	        if (existingUser == null) {
	            model.addAttribute("error", "No user associated with this email address.");
	            return "ForgetPasswordForm";
	        }

	        // Generate a 6-digit PIN
	        Random random = new Random();
	        int vCode = random.nextInt(900000) + 100000;
	        existingUser.setCode(vCode);
	        userService.updateUser(existingUser); 

	        mailUtils.sendEmail(toEmail, "Your Verification Code is: " + vCode);
	        model.addAttribute("message", "A verification code has been sent to your email.");
	        return "Verification";
	    }

	    @GetMapping("/verification")
	    public String verification() {
	        return "Verification";
	    }

	    @PostMapping("/verification")
	    public String postVerification(@RequestParam int code, @RequestParam String email, Model model) {
	        User user = userService.searchbyEmail(email);
	        if (user == null) {
	            model.addAttribute("error", "No user found with the email provided.");
	            return "Verification";
	        }

	        if (code != user.getCode()) {
	            model.addAttribute("error", "Invalid Verification Code");
	            return "Verification";
	        }

	        return "SetPasswordForm";
	    }
	
	    @GetMapping("/setNewPassword")
	    public String getSetNewPassword() {
	    	
	    	return "SetNewPassword";
	    }
	
	    @PostMapping("/setNewPassword")
	    public String postSetNewPassword(@RequestParam String email, @RequestParam String password, Model model) {
	        User user = userService.searchbyEmail(email);
	        if (user == null) {
	            model.addAttribute("error", "Invalid request.");
	            return "UserLoginForm"; // 
	        }

	        user.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
	        userService.updateUser(user);

	        model.addAttribute("success", "Password changed successfully!");
	        return "UserLoginForm";
	    }


}
