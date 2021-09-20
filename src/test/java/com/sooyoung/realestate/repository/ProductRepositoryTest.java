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

import com.sooyoung.realestate.domain.Product;
import com.sooyoung.realestate.domain.enums.ProductStatus;
import com.sooyoung.realestate.domain.enums.ProductType;

@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback
public class ProductRepositoryTest {
	
	private static Long productId;

	@Autowired
	private ProductRepository productRepository;
	
	@BeforeEach
	public void init() {
		Product product = new Product();
		product.setTitle("test_title");
		product.setStatus(ProductStatus.OPEN);
		product.setType(ProductType.REAL_ESTATE);
		product.setTotalInvestingAmount(50000);
		product.setStartedAt(LocalDateTime.now());
		productRepository.save(product);
		this.productId = product.getId();
	}
	
	@Test
	void findById() {
		Product savedProduct = productRepository.findById(productId).get();
		
		assertEquals(productId, savedProduct.getId());
		assertEquals("test_title", savedProduct.getTitle());
		assertEquals(ProductStatus.OPEN, savedProduct.getStatus());
		assertEquals(ProductType.REAL_ESTATE, savedProduct.getType());
		assertEquals(50000, savedProduct.getTotalInvestingAmount());
		assertEquals(0, savedProduct.getCurrentInvestingAmount());
		assertEquals(0, savedProduct.getCurrentUserCnt());		
	}
	
	@Test
	void save() {		
		productRepository.findById(productId).orElseThrow(()->new RuntimeException("Not found product"));
	}
	
	@Test
	void update() {
		Product product = productRepository.findById(productId).orElseThrow(()->new RuntimeException("Not found product"));
		assertEquals(productId, product.getId());
		assertEquals(ProductStatus.OPEN, product.getStatus());
		
		// update
		product.setStatus(ProductStatus.CLOSE);
		Product savedProduct = productRepository.findById(productId).orElseThrow(()->new RuntimeException("Not found product"));
		assertEquals(ProductStatus.CLOSE, savedProduct.getStatus());
	}
	
	@Test
	void delete() {
		Product savedProduct = productRepository.findById(productId).orElseThrow(()->new RuntimeException("Not found product"));
		assertEquals(productId, savedProduct.getId());
		
		productRepository.delete(savedProduct);
		productRepository.flush();
		
		Product deletedProduct = productRepository.findById(productId).orElseGet(()->new Product());
		
		assertNotEquals(productId, deletedProduct.getId());
	}
}
