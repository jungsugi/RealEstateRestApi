package com.sooyoung.realestate.service.user;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sooyoung.realestate.controller.dto.MyInvestDto;
import com.sooyoung.realestate.domain.InvestInfo;
import com.sooyoung.realestate.domain.Product;
import com.sooyoung.realestate.repository.InvestInfoRepository;
import com.sooyoung.realestate.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
	
	private final UserRepository userRepository;
	
	private final InvestInfoRepository investInfoRepository;
	
	public Boolean isExists(long id) {
		return userRepository.findById(id).isPresent();
	}
	
	@Override
	public List<MyInvestDto> getUserProduct(long userId){
		
		List<InvestInfo> InvestInfoList = investInfoRepository.findByUserId(userId);
		
		List<MyInvestDto> result = InvestInfoList.stream()
												.map(i -> new MyInvestDto(i))
												.collect(Collectors.toList());
		
		return result;
	}
	
}

