package com.sooyoung.realestate.domain.enums;

import lombok.Getter;

@Getter
public enum ProductStatus {

	OPEN("10", "Open")
	, CLOSE("20", "Sold Out")
	;
	
	private String code;
	private String name;
	
	private ProductStatus(String code, String name) {
		this.code=code;
		this.name=name;
	}
}
