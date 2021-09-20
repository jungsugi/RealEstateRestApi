package com.sooyoung.kakaopay.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.sooyoung.kakaopay.config.CustomErrorCode;
import com.sooyoung.kakaopay.config.CustomException;
import com.sooyoung.kakaopay.domain.Product;
import com.sooyoung.kakaopay.domain.enums.ProductStatus;
import com.sooyoung.kakaopay.domain.enums.ProductType;
import com.sooyoung.kakaopay.repository.InvestInfoRepository;
import com.sooyoung.kakaopay.repository.InvestLogRepository;
import com.sooyoung.kakaopay.repository.ProductRepository;
import com.sooyoung.kakaopay.repository.UserRepository;
import com.sooyoung.kakaopay.service.invest.InvestServiceImpl;
import com.sooyoung.kakaopay.service.product.ProductService;
import com.sooyoung.kakaopay.service.product.ProductServiceImpl;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@Rollback
public class ProductServiceTest {

		ProductService productService;
				
	    @Autowired
		private ProductRepository productRepository;
		
	    public static long testProductId;
	    public static long testUserId;
	    
		private static final String SUCCESS = "SUCCESS";
		private static final String SOLD_OUT = "SOLD OUT";
		private static final String CAN_NOT_OVER_MAX_AMOUNT = "CAN NOT OVER MAX AMOUNT";
		
	    @BeforeEach
	    public void beforeEach() {
	    	productService = new ProductServiceImpl(productRepository);
	    	
	    	Product product = new Product();
	    	product.setStartedAt(LocalDateTime.now());
	    	product.setFinishedAt(LocalDateTime.parse("2029-12-03T10:15:30"));
	    	product.setType(ProductType.REAL_ESTATE);
	    	product.setTotalInvestingAmount(1000000);	//max 1000000
	    	product.setTitle("test_data_1");
	    	product.setStatus(ProductStatus.OPEN);
	    	productRepository.save(product);
	    	
	    	testProductId = product.getId();
	    }
    
  
    /* 조회 test1 : Product가 진행중인 투자기간이 아닐때 */
    @Test
    public void Test1() {
    	Product product = productRepository.findById(testProductId).get();
    	product.setFinishedAt(LocalDateTime.parse("2020-12-03T10:15:30"));
    	
    	Optional<Product> product_temp = productRepository.findByTimeAndProductId(LocalDateTime.now(),testProductId);
		
    	assertThat(!product_temp.isPresent());
    }
    
    
    /* 조회 test2 : Product가 진행중인 투자기간 일때 */
    @Test
    public void Test2() {
    	Optional<Product> product = productRepository.findById(testProductId);
		
    	assertThat(product.isPresent());
    	assertThat(product.get().getTitle()).isEqualTo("test_data_1");
    }
     
}
