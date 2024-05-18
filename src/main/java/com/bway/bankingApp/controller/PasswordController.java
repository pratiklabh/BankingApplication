package com.bway.bankingApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.bway.bankingApp.model.Password;
import com.bway.bankingApp.model.User;
import com.bway.bankingApp.service.PasswordService;
import com.bway.bankingApp.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class PasswordController {

	@Autowired
	private PasswordService passwordService;

	@Autowired
	private UserService userService;

	@GetMapping("/changeUserPassword")
	public String getChangePassword(HttpSession session) {

		if (session.getAttribute("validUser") == null) {

			return "UserLoginForm";

		}

		return "ChangeUserPassword";
	}

	@PostMapping("/changeUserPassword")
	public String postChangePassword(@ModelAttribute Password password, Model model, HttpSession session) {

		String accountnumber = (String) session.getAttribute("accountnumber");

		User user = userService.searchAccountnumber(accountnumber);

		password.setOldPassword(DigestUtils.md5DigestAsHex(password.getOldPassword().getBytes()));
		password.setNewPassword(DigestUtils.md5DigestAsHex(password.getNewPassword().getBytes()));
		password.setConfirmNewPassword(DigestUtils.md5DigestAsHex(password.getConfirmNewPassword().getBytes()));

		if (!user.getPassword().equals(password.getOldPassword())) {

			model.addAttribute("error", "You entered wrong password!!!");
			return "ChangeUserPassword";

		}

		if (!password.getNewPassword().equals(password.getConfirmNewPassword())) {

			model.addAttribute("error", "The new passwords do not match!!!");
			return "ChangeUserPassword";
		}

		if (user.getPassword().equals(password.getNewPassword())) {
			model.addAttribute("error", "Old password and New passwords cannot be same !!!");
			return "ChangeUserPassword";
		}

		passwordService.changePassword(accountnumber, password.getNewPassword());
		model.addAttribute("message", "Your password has been changed!!!");
		return "ChangeUserPassword";
	}

}
