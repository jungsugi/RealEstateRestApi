package com.sooyoung.realestate.service.invest;

import com.sooyoung.realestate.controller.dto.InvestDto;

public interface InvestService {

	
	public InvestDto investProduct(long userId, long productId, long investAmount) throws Exception;
	
	
}
