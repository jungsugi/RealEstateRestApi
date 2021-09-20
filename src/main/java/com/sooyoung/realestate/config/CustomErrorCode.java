package com.sooyoung.realestate.config;

import org.springframework.http.HttpStatus;

import lombok.Getter;


@Getter
public enum CustomErrorCode {
	
	INVAILD_USER_ID_REQUEST_HEADER(HttpStatus.BAD_REQUEST, "101", "Invaild userId request header !")
	, CAN_NOT_FIND_USER_ID(HttpStatus.NOT_FOUND, "102", "can't find userId !")
	, CAN_NOT_FIND_PRODUCT_ID(HttpStatus.NOT_FOUND, "103", "can't find proceed ProductId !")
	, CAN_NOT_OVER_MAX_AMOUNT(HttpStatus.BAD_REQUEST, "104", "can't over invest Max Amount !")
	, NOT_FOUND_PRODUCT_LIST(HttpStatus.NOT_FOUND, "105", "can't find products !")
	, CAN_NOT_INVEST_SOLDOUT_PRODUCT(HttpStatus.BAD_REQUEST, "106", "can't invest sold out product !")
	;
	
	private HttpStatus status;
	private String code;
	private String message;
	
	
	private CustomErrorCode(HttpStatus status, String code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}
}
