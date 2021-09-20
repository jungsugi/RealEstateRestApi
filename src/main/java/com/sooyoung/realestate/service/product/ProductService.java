package com.sooyoung.realestate.service.product;

import java.time.LocalDateTime;
import java.util.List;

import com.sooyoung.realestate.controller.dto.ProductDto;

public interface ProductService {

	List<ProductDto> findAll();
	
	List<ProductDto> findCurrentProductList();
}
