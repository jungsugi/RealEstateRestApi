package com.sooyoung.realestate.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sooyoung.realestate.controller.dto.InvestDto;
import com.sooyoung.realestate.controller.dto.MyInvestDto;
import com.sooyoung.realestate.controller.dto.ProductDto;
import com.sooyoung.realestate.controller.dto.UserDto;
import com.sooyoung.realestate.service.invest.InvestService;
import com.sooyoung.realestate.service.product.ProductServiceImpl;
import com.sooyoung.realestate.service.user.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j		//log 객체를 사용할 수 있게 해줌.
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductContoller {
	
	private final ProductServiceImpl productService;
	private final InvestService investService;
	private final UserService userService;
	/**
	 * 
	 *  1-1 전체 투자상품 조회
	 *  GET /products
	 * 	상품모집기간(started_at,finished_at)내의상품만응답합니다.
	 * 	전체투자상품응답은다음내용을포함합니다.
	 * 	상품ID,상품제목,총모집금액,현재모집금액,투자자수,투자모집상태(모집중,모집완료),상품모집기간
	 * 	GET /products -> 전체상품조회 
	 * @param user
	 * @return
	 */
	@GetMapping("/products")
	public List<ProductDto> getProducts(UserDto userDto) {
		return productService.findCurrentProductList();
	}
	

	/**
	 * 2.상품 투자하기 
	 * POST /products/{productId}
	 * :사용자식별값,상품ID,투자금액을입력값으로받습니다.
	 *  총투자모집금액(total_investing_amount)을넘어서면sold-out상태를응답합니다.
	 * 
	 * ex) http://localhost:8080/api/products/1/my?amount=3000000
	 * 
	 * @param user
	 * @param productId
	 * @param investDto
	 * @return
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	@PostMapping("/products/{productId}/my")
	public InvestDto investProduct(UserDto userDto, @PathVariable String productId, @RequestParam String amount) throws NumberFormatException, Exception {
		return investService.investProduct(userDto.getId(), Long.valueOf(productId), Long.valueOf(amount));
	}
	
	/**
	 * 3.나의투자상품조회API
	 * GET /products/my
	 * 다음의요건을만족하는나의투자상품조회API를작성해주세요.
	 * 내가투자한모든상품을반환합니다.
	 * 나의투자상품응답은다음내용을포함합니다.
	 * 상품ID,상품제목,총모집금액,나의투자금액,투자일시 
	 * 
	 * @param userDto
	 * @return
	 */
	@GetMapping("/products/my")
	public List<MyInvestDto> myProduct(UserDto userDto) {
		return userService.getUserProduct(userDto.getId());
	}
	
}
