package com.sooyoung.realestate.config;

public class CustomException extends RuntimeException {	//RuntimeException μμ μ£Όμ!!
	
	String message;
	String code;
	
	public CustomException(CustomErrorCode errorCode) {
		super(errorCode.getMessage());
		this.message=errorCode.getMessage();
		this.code=errorCode.getCode();
	}
}
