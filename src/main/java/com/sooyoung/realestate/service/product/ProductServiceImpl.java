package com.sooyoung.realestate.service.product;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sooyoung.realestate.controller.dto.ProductDto;
import com.sooyoung.realestate.domain.Product;
import com.sooyoung.realestate.repository.InvestInfoRepository;
import com.sooyoung.realestate.repository.InvestLogRepository;
import com.sooyoung.realestate.repository.ProductRepository;
import com.sooyoung.realestate.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;
	
	@Override
	public List<ProductDto> findAll() {
		List<Product> products = productRepository.findAll();		
		
		List<ProductDto> result = products.stream()
				.map(p->new ProductDto(p))
				.collect(Collectors.toList());
		
		return result;
	}
	
	
	@Override
	public List<ProductDto> findCurrentProductList() {
		LocalDateTime time = LocalDateTime.now();
		List<Product> products = productRepository.findByTime(time);		
		
		List<ProductDto> result = products.stream()
				.map(p->new ProductDto(p))
				.collect(Collectors.toList());
		
		return result;
	}
	
}
