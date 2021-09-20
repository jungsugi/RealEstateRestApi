package com.sooyoung.realestate.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sooyoung.realestate.domain.InvestLog;

public interface InvestLogRepository extends JpaRepository<InvestLog, Long>{
		
}
