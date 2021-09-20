package com.sooyoung.realestate.domain.enums;

import lombok.Getter;

@Getter
public enum InvestLogResultStatus {

	SUCCESS("SUCCESS","Invest Complete")
	,FAIL("FAIL","Invest Fail")
	;
	
	private String code;
	private String name;
	
	private InvestLogResultStatus(String code, String name) {
		this.code=code;
		this.name=name;
	}
	
}
