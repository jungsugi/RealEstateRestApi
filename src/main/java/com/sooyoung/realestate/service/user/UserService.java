package com.sooyoung.realestate.service.user;

import java.util.List;

import com.sooyoung.realestate.controller.dto.MyInvestDto;

public interface UserService {

	//user Exists_YN
	public Boolean isExists(long id);
	
	//retrieve Product
	public List<MyInvestDto> getUserProduct(long userId);
	
	
}
