package com.sooyoung.realestate.domain.enums;

import lombok.Getter;

@Getter
public enum ProductType {

	REAL_ESTATE("10","Real_estate")
	, CREDIT("20","Credit")
	;
	
	private String code;
	private String name;
	
	ProductType(String code, String name) {
		this.code = code;
		this.name = name;
	}
}
