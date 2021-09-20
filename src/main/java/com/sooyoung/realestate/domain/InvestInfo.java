package com.sooyoung.realestate.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@Table(name="invest_info")
public class InvestInfo {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="product_id", referencedColumnName = "id")
	private Product product;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_id", referencedColumnName = "id")
	private User user;
	
	
	@Column(name="invest_amount")
	private long investAmount;

	@Column(name="invest_time")
	private LocalDateTime investTime;

	@Column(name="bigo")
	private String bigo;
	
	@Column(name="create_date")
	@CreationTimestamp
	private LocalDateTime createDate;	
	
	@Column(name="mod_date")
	@UpdateTimestamp
	private LocalDateTime modDate;
	
}
