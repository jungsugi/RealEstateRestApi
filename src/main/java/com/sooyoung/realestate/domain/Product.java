package com.sooyoung.realestate.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.sooyoung.realestate.domain.enums.ProductStatus;
import com.sooyoung.realestate.domain.enums.ProductType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="product")
@Data
@NoArgsConstructor
public class Product {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private long id;
	
	@Column(name="title")
	private String title;
	
	@ColumnDefault("0")
	@Column(name="total_investing_amount")
	private long totalInvestingAmount;
	
	@ColumnDefault("0")
	@Column(name="current_investing_amount") 
	private long currentInvestingAmount;

	@ColumnDefault("0")
	@Column(name="current_user_cnt") 
	private int currentUserCnt;

	@Column(name="type") 
	@Enumerated(EnumType.STRING)
	private ProductType type;

	@Column(name="status")
	@Enumerated(EnumType.STRING)
	private ProductStatus status;
		
	@Column(name="started_at")
	private LocalDateTime startedAt;
	
	@Column(name="finished_at")
	private LocalDateTime finishedAt;
	
	@Column(name="create_date")
	@CreationTimestamp
	private LocalDateTime createDate;	
	
	@Column(name="mod_date")
	@UpdateTimestamp
	private LocalDateTime modDate;
	
}
