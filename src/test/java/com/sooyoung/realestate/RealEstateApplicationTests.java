package com.sooyoung.realestate;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.sooyoung.realestate.constants.Descriptor;
import com.sooyoung.realestate.domain.Product;
import com.sooyoung.realestate.domain.User;
import com.sooyoung.realestate.domain.enums.ProductStatus;
import com.sooyoung.realestate.domain.enums.ProductType;
import com.sooyoung.realestate.repository.ProductRepository;
import com.sooyoung.realestate.repository.UserRepository;
	
@Transactional
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest // webEnvironment Defualt Mock, MockMvc
@Rollback
public class RealEstateApplicationTests {

	private static final String X_USER_ID = "X-USER-ID";
	private static final String AMOUNT = "amount";
	private static final String TEST_AMOUNT = "1000";
	
	private static Long testUserId;
	private static Long testProductId;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	private MockMvc mockMvc;

	@BeforeEach // @Before
	public void setUp(WebApplicationContext context, RestDocumentationContextProvider documentationContextProvider) {

		this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
				.apply(documentationConfiguration(documentationContextProvider))
				.build();
		
		Product product = new Product();
		product.setTitle("부동산투자");
		product.setStatus(ProductStatus.OPEN);
		product.setType(ProductType.REAL_ESTATE);
		product.setTotalInvestingAmount(50000);
		product.setStartedAt(LocalDateTime.now());
		product.setFinishedAt(LocalDateTime.now().plusMinutes(1L));
		productRepository.save(product);
		this.testProductId = product.getId();
		
		User user = new User();
		user.setUserName("통합테스트유저");
		userRepository.save(user);
		this.testUserId = user.getId();
	}
	
	@Test
	@DisplayName(value = "/api/products")
	public void getProducts() throws Exception {
		this.mockMvc.perform(get("/api/products")
					.header(X_USER_ID, testUserId)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("/success-get-products", responseFields(Descriptor.getProductsResponseFields)));
	}
	
	@Test
	@DisplayName(value = "/api/products/my")
	public void getProduct() throws Exception {
		this.mockMvc.perform(get("/api/products/my")
					.header(X_USER_ID, testUserId)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("/success-get-my-products", responseFields(Descriptor.getMyProductsResponseFields)));
	}
	
	@Test
	@DisplayName(value = "/api/products/{productId}/my")
	public void investProduct() throws Exception {
		this.mockMvc.perform(post("/api/products/{productId}/my", testProductId)
					.param(AMOUNT, TEST_AMOUNT)
					.header(X_USER_ID, testUserId)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("/success-invest-product", responseFields(Descriptor.investProductResponseFields)));
	}
}
