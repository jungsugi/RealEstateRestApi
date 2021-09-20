package com.sooyoung.realestate.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.sooyoung.realestate.domain.User;

@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback
public class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;
	
	private static Long userId;
	
	@BeforeEach
	void init() {
		User user = new User();
		user.setUserName("test");
		userRepository.save(user);
		this.userId = user.getId();
	}
	
	@Test
	void findById() {
		User savedUser = userRepository.findById(userId).get();
		
		assertEquals(userId, savedUser.getId());
		assertEquals("test", savedUser.getUserName());		
	}
	
	@Test
	void save() {		
		userRepository.findById(userId).orElseThrow(()->new RuntimeException("Not found user"));
	}
	
	@Test
	void update() {
		User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("Not found user"));
		assertEquals(userId, user.getId());
		
		// update
		user.setUserName("test2");
		User savedUser = userRepository.findById(userId).orElseThrow(()->new RuntimeException("Not found user"));
		assertEquals("test2", savedUser.getUserName());
	}
	
	@Test
	void delete() {
		User savedUser = userRepository.findById(userId).orElseThrow(()->new RuntimeException("Not found user"));
		assertEquals(userId, savedUser.getId());
		
		userRepository.delete(savedUser);
		userRepository.flush();
		
		User deletedUser = userRepository.findById(userId).orElseGet(()-> new User());
		
		assertNotEquals(userId, deletedUser.getId());
	}
}
