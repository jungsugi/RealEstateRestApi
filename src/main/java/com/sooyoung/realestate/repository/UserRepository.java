package com.sooyoung.realestate.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sooyoung.realestate.domain.User;


public interface UserRepository extends JpaRepository<User, Long>{	
	
}
