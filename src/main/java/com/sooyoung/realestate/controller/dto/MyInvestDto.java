package com.sooyoung.realestate.controller.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.sooyoung.realestate.domain.InvestInfo;

import lombok.Data;

@Data
public class MyInvestDto implements Serializable  {
	
	private long product_id;	// 상품id
	private String title;  // 상품명
	private long total_investing_amount;	// 총모집금액
	private long my_invest_amount;	// 나의 투자금액
	private LocalDateTime invest_date;	// 투자 일시
	
	public MyInvestDto(InvestInfo i) {
		this.product_id = i.getProduct().getId();
		this.title = i.getProduct().getTitle();
		this.total_investing_amount = i.getProduct().getTotalInvestingAmount();	//총투자모집금액
		this.my_invest_amount = i.getInvestAmount();
		this.invest_date = i.getInvestTime();
	}
	
}
