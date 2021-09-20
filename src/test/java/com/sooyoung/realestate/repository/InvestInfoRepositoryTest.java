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
import com.sooyoung.realestate.domain.Product;
import com.sooyoung.realestate.domain.User;
import com.sooyoung.realestate.domain.enums.ProductStatus;
import com.sooyoung.realestate.domain.enums.ProductType;

@DataJpaTest
@Transactional 
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback
public class InvestInfoRepositoryTest {

	private static Long productId;
	private static Long userId;
	private static Long investInfoId;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private InvestInfoRepository investInfoRepository;
	
	@BeforeEach
	public void init() {
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
	}
	
	@Test
	void findById() {
		InvestInfo investInfo = investInfoRepository.findById(investInfoId)
				.orElseThrow(()->new RuntimeException("Not found investInfo"));
		
		assertEquals(investInfoId, investInfo.getId());
		assertEquals(1000, investInfo.getInvestAmount());
		
		assertEquals(productId, investInfo.getProduct().getId());
		assertEquals("product_test_title", investInfo.getProduct().getTitle());
		assertEquals(ProductStatus.OPEN, investInfo.getProduct().getStatus());
		assertEquals(ProductType.REAL_ESTATE, investInfo.getProduct().getType());
		assertEquals(50000, investInfo.getProduct().getTotalInvestingAmount());
		
		assertEquals(userId, investInfo.getUser().getId());
		assertEquals("test", investInfo.getUser().getUserName());
	}
	
	@Test
	void save() {
		investInfoRepository.findById(investInfoId).orElseThrow(()->new RuntimeException("Not found investInfo"));
	}
	
	@Test
	void update() {
		InvestInfo investInfo = investInfoRepository.findById(investInfoId).orElseThrow(()->new RuntimeException("Not found investInfo"));
		assertEquals(investInfoId, investInfo.getId());
		
		// update
		investInfo.setInvestAmount(2000);
		
		InvestInfo savedInvestInfo = investInfoRepository.findById(investInfoId).orElseThrow(()->new RuntimeException("Not found investInfo"));
		assertEquals(2000, savedInvestInfo.getInvestAmount());
	}
	
	@Test
	void delete() {
		InvestInfo savedInvestInfo = investInfoRepository.findById(investInfoId).orElseThrow(()->new RuntimeException("Not found investInfo"));
		assertEquals(investInfoId, savedInvestInfo.getId());
		
		// delete
		investInfoRepository.delete(savedInvestInfo);
		investInfoRepository.flush();
		
		InvestInfo deletedInvestInfo = investInfoRepository.findById(investInfoId).orElseGet(()->new InvestInfo());
		
		assertNotEquals(investInfoId, deletedInvestInfo.getId());
	}
}
