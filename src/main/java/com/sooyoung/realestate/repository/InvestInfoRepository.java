package com.sooyoung.realestate.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sooyoung.realestate.domain.InvestInfo;

public interface InvestInfoRepository extends JpaRepository<InvestInfo, Long>{
	
	Optional<InvestInfo> findByUserIdAndProductId(long userId, long productId);
	
	List<InvestInfo> findByUserId(long userId);
	
}
