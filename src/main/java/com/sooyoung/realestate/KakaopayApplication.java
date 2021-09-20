package com.sooyoung.realestate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class KakaopayApplication {

	public static void main(String[] args) {
		SpringApplication.run(KakaopayApplication.class, args);
	}

}
