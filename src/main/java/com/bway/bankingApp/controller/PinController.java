package com.bway.bankingApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.bway.bankingApp.model.Pin;
import com.bway.bankingApp.model.User;
import com.bway.bankingApp.service.PinService;
import com.bway.bankingApp.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class PinController {

	@Autowired
	private PinService pinService;

	@Autowired
	private UserService userService;

	@GetMapping("/changeUserPin")
	public String getChangePin(HttpSession session) {

		if (session.getAttribute("validUser") == null) {

			return "UserLoginForm";

		}

		return "ChangeUserPin";
	}

	@PostMapping("/changeUserPin")
	public String postChangePin(@ModelAttribute Pin pin, Model model, HttpSession session) {

		String accountnumber = (String) session.getAttribute("accountnumber");

		User user = userService.searchAccountnumber(accountnumber);
		pin.setOldPin(DigestUtils.md5DigestAsHex(pin.getOldPin().getBytes()));
		pin.setNewPin(DigestUtils.md5DigestAsHex(pin.getNewPin().getBytes()));
		pin.setConfirmNewPin(DigestUtils.md5DigestAsHex(pin.getConfirmNewPin().getBytes()));

		if (!user.getPin().equals(pin.getOldPin())) {

			model.addAttribute("error", "You entered wrong pin!!!");
			return "ChangeUserPin";

		}

		if (!pin.getNewPin().equals(pin.getConfirmNewPin())) {

			model.addAttribute("error", "The new pins do not match!!!");
			return "ChangeUserPin";
		}

		if (user.getPin().equals(pin.getNewPin())) {
			model.addAttribute("error", "Old pin and New pin cannot be same !!!");
			return "ChangeUserPin";
		}

		pinService.changePin(accountnumber, pin.getNewPin());
		model.addAttribute("message", "Your pin has been changed!!!");
		return "ChangeUserPin";
	}

}
