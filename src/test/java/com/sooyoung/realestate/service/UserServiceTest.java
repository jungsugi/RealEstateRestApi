package com.sooyoung.kakaopay.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.sooyoung.kakaopay.controller.dto.MyInvestDto;
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
import com.sooyoung.kakaopay.service.user.UserService;
import com.sooyoung.kakaopay.service.user.UserServiceImpl;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@Rollback
public class UserServiceTest {

	UserService userService;
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
    
	private static final String SUCCESS = "SUCCESS";
	private static final String SOLD_OUT = "SOLD OUT";
	private static final String CAN_NOT_OVER_MAX_AMOUNT = "CAN NOT OVER MAX AMOUNT";
	
    @BeforeEach
    public void beforeEach() {
    	userService = new UserServiceImpl(userRepository,investInfoRepository);
    	InvestService = new InvestServiceImpl(investLogRepository,investInfoRepository,userRepository,productRepository);
    	
    	Product product = new Product();
    	product.setStartedAt(LocalDateTime.now());
    	product.setFinishedAt(LocalDateTime.parse("2029-12-03T10:15:30"));
    	product.setType(ProductType.REAL_ESTATE);
    	product.setTotalInvestingAmount(3000000);	//max 3000000
    	product.setTitle("test_data_1");
    	product.setStatus(ProductStatus.OPEN);
    	productRepository.save(product);

    	User user = new User();
    	user.setUserName("testUser");
    	userRepository.save(user);
    	
    	testProductId = product.getId();
    	testUserId = user.getId();
    }
	
    @Test
    public void Test() {
    	boolean flag = userService.isExists(testUserId);
    	
    	assertThat(flag).isEqualTo(true);
    }
    
	/* 조회 test : user1 이 product1 에 투자 후 user1의 투자중 상품 조회*/
	@Test
	public void Test2() {
		Optional<Product> product = productRepository.findById(testProductId);
		
		assertThat(product.isPresent());
		assertThat(product.get().getTitle()).isEqualTo("test_data_1");
		
		try {
			InvestService.investProduct(testUserId, testProductId, 1250000L);	
			InvestService.investProduct(testUserId, testProductId, 250000L);
		}
		catch (Exception e) {
			fail("Occurr error");
			e.printStackTrace();
		}
		
		List<MyInvestDto> userProductList = userService.getUserProduct(testUserId);

		assertThat(userProductList.size()).isEqualTo(1);
		assertThat(userProductList.get(0).getTitle()).isEqualTo("test_data_1");
		assertThat(userProductList.get(0).getTotal_investing_amount()).isEqualTo(3000000L); //총투자모집금액
		assertThat(userProductList.get(0).getMy_invest_amount()).isEqualTo(1500000L); //나의 투자모집금액
	}

}
