package com.sooyoung.kakaopay.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sooyoung.kakaopay.config.CustomErrorCode;
import com.sooyoung.kakaopay.config.CustomException;
import com.sooyoung.kakaopay.controller.dto.InvestDto;
import com.sooyoung.kakaopay.domain.Product;
import com.sooyoung.kakaopay.domain.User;
import com.sooyoung.kakaopay.domain.enums.ProductStatus;
import com.sooyoung.kakaopay.domain.enums.ProductType;
import com.sooyoung.kakaopay.repository.InvestInfoRepository;
import com.sooyoung.kakaopay.repository.InvestLogRepository;
import com.sooyoung.kakaopay.repository.ProductRepository;
import com.sooyoung.kakaopay.repository.UserRepository;
import com.sooyoung.kakaopay.service.invest.InvestService;
import com.sooyoung.kakaopay.service.invest.InvestServiceImpl;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@Rollback
public class InvestServiceTest {

    InvestService InvestService;
    
    @Autowired
	private InvestLogRepository investLogRepository;
    @Autowired
	private InvestInfoRepository investInfoRepository;    
    @Autowired
	private UserRepository userRepository;
    @Autowired	
	private ProductRepository productRepository;
	
    public static long testProductId;
    public static long testUserId;
    public static long testUserId2;
    
	private static final String SUCCESS = "SUCCESS";
	private static final String SOLD_OUT = "SOLD OUT";
	private static final String CAN_NOT_OVER_MAX_AMOUNT = "CAN NOT OVER MAX AMOUNT";
	
    @BeforeEach
    public void beforeEach() {
    	InvestService = new InvestServiceImpl(investLogRepository,investInfoRepository,userRepository,productRepository);
    	
    	Product product = new Product();
    	product.setStartedAt(LocalDateTime.now());
    	product.setFinishedAt(LocalDateTime.parse("2023-12-03T10:15:30"));
    	product.setType(ProductType.REAL_ESTATE);
    	product.setTotalInvestingAmount(1000000);	//max 1000000
    	product.setTitle("test_data_1");
    	product.setStatus(ProductStatus.OPEN);
    	productRepository.save(product);
    	
    	User user = new User();
    	user.setUserName("testUser");
    	userRepository.save(user);
    	
    	User user2 = new User();
    	user2.setUserName("testUser");
    	userRepository.save(user2);
    	
    	
    	testProductId = product.getId();
    	testUserId = user.getId();
    	testUserId2 = user2.getId();
    }
   
    @AfterEach
    public void afterEach() {
    
    }
    
    /* 다 인원 입금 test1 */
    @Test
    public void investTest() {
    	//test product 생성
    	try {        	
    		Product testProduct = productRepository.findById(testProductId).get();
    		
    		InvestDto result1 = InvestService.investProduct(testUserId, testProduct.getId(), 200000L);
    		InvestDto result2 = InvestService.investProduct(testUserId2, testProduct.getId(), 300000L);
    		InvestDto result3 = InvestService.investProduct(testUserId2, testProduct.getId(), 400000L);
    		
    		InvestDto result1Expect = new InvestDto(SUCCESS);
    		InvestDto result2Expect = new InvestDto(SUCCESS);
    		InvestDto result3Expect = new InvestDto(SUCCESS);

    		assertThat(result1).isEqualTo(result1Expect);
    		assertThat(result2).isEqualTo(result2Expect);
    		assertThat(result3).isEqualTo(result3Expect);	
    		
    		assertThat(investInfoRepository.findByUserIdAndProductId(testUserId,testProductId).get().getInvestAmount()).isEqualTo(200000L);
    		assertThat(investInfoRepository.findByUserIdAndProductId(testUserId2,testProductId).get().getInvestAmount()).isEqualTo(700000L);

    		assertThat(productRepository.findById(testProductId).get().getStatus()).isEqualTo(ProductStatus.OPEN);
    		assertThat(productRepository.findById(testProductId).get().getStatus()).isEqualTo(ProductStatus.OPEN);
    	}
    	catch(Exception E){
    		fail("Occurr error");
    		E.printStackTrace();
    	}
    }
    
    
    /* 최대금액 입금 test 2*/
    @Test
    public void investTest2() {
    	//test product 생성
    	try {        	
    		Product testProduct = productRepository.findById(testProductId).get();
    		
    		InvestDto result = InvestService.investProduct(testUserId, testProduct.getId(), 1000000);
    		InvestDto expect = new InvestDto(SUCCESS);

    		assertThat(result).isEqualTo(expect);
    		assertThat(productRepository.findById(testProductId).get().getStatus()).isEqualTo(ProductStatus.CLOSE);
    		
    		
    		InvestDto result2 = InvestService.investProduct(testUserId2, testProduct.getId(), 2000000);
    		InvestDto expect2 = new InvestDto(SOLD_OUT);
    		
    		assertThat(result2).isEqualTo(expect2);
    		assertThat(!investInfoRepository.findByUserIdAndProductId(testUserId2,testProductId).isPresent());
    	}
    	catch(Exception E){
    		fail("Occurr error");
    		E.printStackTrace();
    	}
    }
    
    /* 입금 test 3 : max 금액초과*/
    @Test
    public void investTest3() {
    	//test product 생성
    	try {        	
    		Product testProduct = productRepository.findById(testProductId).get();
    		
    		InvestDto result = InvestService.investProduct(testUserId, testProduct.getId(), 5000000);
    		InvestDto expect = new InvestDto(CAN_NOT_OVER_MAX_AMOUNT);

    		assertThat(result).isEqualTo(expect);
    	}
    	catch(Exception E){
    		fail("Occurr error");
    		E.printStackTrace();
    	}
    }
    
    
    /* 입금 test 4 : Product가 진행중인 투자기간이 아닐때 */
    @Test
    public void investTest4() {
		Product testProduct = productRepository.findById(testProductId).get();
		testProduct.setFinishedAt(testProduct.getStartedAt());
		
		CustomException exception = Assertions.assertThrows(CustomException.class, ()-> InvestService.investProduct(testUserId, testProduct.getId(), 1000000));

		String message = exception.getMessage();
		assertThat(CustomErrorCode.CAN_NOT_FIND_PRODUCT_ID.getMessage()).isEqualTo(message);	
    }
    
}


