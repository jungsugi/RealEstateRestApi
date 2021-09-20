package com.sooyoung.realestate.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.sooyoung.realestate.domain.InvestInfo;
import com.sooyoung.realestate.domain.InvestLog;
import com.sooyoung.realestate.domain.Product;
import com.sooyoung.realestate.domain.User;
import com.sooyoung.realestate.domain.enums.InvestLogResultStatus;
import com.sooyoung.realestate.domain.enums.ProductStatus;
import com.sooyoung.realestate.domain.enums.ProductType;

@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback
class InvestLogRepositoryTest {
	
	private static Long productId;
	private static Long userId;
	private static Long investInfoId;
	private static Long investLogId;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private InvestInfoRepository investInfoRepository;
	
	@Autowired
	private InvestLogRepository investLogRepository;
	
	@BeforeEach
	void init() {
		Product product = new Product();
		product.setTitle("product_test_title");
		product.setStatus(ProductStatus.OPEN);
		product.setType(ProductType.REAL_ESTATE);
		product.setTotalInvestingAmount(50000);
		product.setStartedAt(LocalDateTime.now());
		productRepository.save(product);
		this.productId = product.getId();
		
		User user = new User();
		user.setUserName("test");
		userRepository.save(user);
		this.userId = user.getId();
		
		InvestInfo investInfo = new InvestInfo();
		investInfo.setInvestAmount(1000);
		investInfo.setUser(user);
		investInfo.setProduct(product);
		investInfo.setInvestTime(LocalDateTime.now());
		investInfoRepository.save(investInfo);
		this.investInfoId = investInfo.getId();
		
		InvestLog investLog = new InvestLog();
		investLog.setInvestAmount(1000);
		investLog.setUser(user);
		investLog.setProduct(product);
		investLog.setInvestInfo(investInfo);
		investLog.setInvestFlagCd("I");
		investLog.setInvestTime(LocalDateTime.now());
		investLog.setInvestResultStatusCd(InvestLogResultStatus.SUCCESS);
		investLogRepository.save(investLog);
		this.investLogId = investLog.getId();
	}

	@Test
	void findById() {
		InvestLog investLog = investLogRepository.findById(investLogId)
				.orElseThrow(()->new RuntimeException("Not found investLog"));
		
		// product
		assertEquals(productId, investLog.getProduct().getId());
		assertEquals("product_test_title", investLog.getProduct().getTitle());
		assertEquals(ProductStatus.OPEN, investLog.getProduct().getStatus());
		assertEquals(ProductType.REAL_ESTATE, investLog.getProduct().getType());
		assertEquals(50000, investLog.getProduct().getTotalInvestingAmount());
		
		// user 
		assertEquals(userId, investLog.getUser().getId());
		assertEquals("test", investLog.getUser().getUserName());
		
		// investInfo
		assertEquals(investInfoId, investLog.getInvestInfo().getId());
		assertEquals(1000, investLog.getInvestInfo().getInvestAmount());
		
		// investLog
		assertEquals(investLogId, investLog.getId());
		assertEquals(InvestLogResultStatus.SUCCESS, investLog.getInvestResultStatusCd());
		assertEquals("I", investLog.getInvestFlagCd());
	}
	
	@Test
	void save() {
		investLogRepository.findById(investLogId).orElseThrow(()->new RuntimeException("Not found investLog"));
	}
	
	@Test
	void update() {
		InvestLog investLog = investLogRepository.findById(investLogId).orElseThrow(()->new RuntimeException("Not found investLog"));
		assertEquals(investLogId, investLog.getId());
		
		// update
		investLog.setInvestAmount(2000);
		
		InvestLog savedInvestLog = investLogRepository.findById(investLogId).orElseThrow(()->new RuntimeException("Not found investLog"));
		assertEquals(2000, investLog.getInvestAmount());
	}
	
	@Test
	void delete() {
		InvestLog savedInvestLog = investLogRepository.findById(investLogId).orElseThrow(()->new RuntimeException("Not found investLog"));
		assertEquals(investLogId, savedInvestLog.getId());
		
		// delete
		investLogRepository.delete(savedInvestLog);
		investLogRepository.flush();
			
		InvestLog deletedInvestLog = investLogRepository.findById(investLogId).orElseGet(()->new InvestLog());
		
		assertNotEquals(investLogId, deletedInvestLog.getId());
	}
}
