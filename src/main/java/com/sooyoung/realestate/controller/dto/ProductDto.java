package com.sooyoung.realestate.controller.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.sooyoung.realestate.domain.Product;

import lombok.Data;

@Data
public class ProductDto implements Serializable {
	
	private Long product_id;
	private String title;
	private Long total_investing_amount;
	private Long current_invest_amount;
	private Integer current_user_cnt;
	private String status;
	private LocalDateTime started_at;
	private LocalDateTime finished_at;

	public ProductDto(Product product) {
		this.product_id=product.getId();
		this.title=product.getTitle();
		this.total_investing_amount=product.getTotalInvestingAmount();
		this.current_invest_amount=product.getCurrentInvestingAmount();
		this.current_user_cnt=product.getCurrentUserCnt();
		this.status=product.getStatus().getName();
		this.started_at=product.getStartedAt();
		this.finished_at=product.getFinishedAt();
	}
}
