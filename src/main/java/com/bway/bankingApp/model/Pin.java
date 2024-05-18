package com.bway.bankingApp.model;


import lombok.Data;

@Data
public class Pin {
	private int id;
	private String oldPin;
	private String newPin;
	private String confirmNewPin;

}
