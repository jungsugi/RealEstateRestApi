package com.sooyoung.realestate.controller.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class InvestDto implements Serializable {

	private String message;
	
	public InvestDto(String message) {
		this.message = message;
	}
}
