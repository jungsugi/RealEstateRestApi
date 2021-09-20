package com.sooyoung.realestate.service.invest;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sooyoung.realestate.config.CustomErrorCode;
import com.sooyoung.realestate.config.CustomException;
import com.sooyoung.realestate.controller.dto.InvestDto;
import com.sooyoung.realestate.domain.InvestInfo;
import com.sooyoung.realestate.domain.InvestLog;
import com.sooyoung.realestate.domain.Product;
import com.sooyoung.realestate.domain.User;
import com.sooyoung.realestate.domain.enums.InvestLogResultStatus;
import com.sooyoung.realestate.domain.enums.ProductStatus;
import com.sooyoung.realestate.repository.InvestInfoRepository;
import com.sooyoung.realestate.repository.InvestLogRepository;
import com.sooyoung.realestate.repository.ProductRepository;
import com.sooyoung.realestate.repository.UserRepository;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class InvestServiceImpl implements InvestService{
	
	private static final String SUCCESS = "SUCCESS";
	private static final String SOLD_OUT = "SOLD OUT";
	private static final String CAN_NOT_OVER_MAX_AMOUNT = "CAN NOT OVER MAX AMOUNT";
	
	private final InvestLogRepository investLogRepository;
	
	private final InvestInfoRepository investInfoRepository;

	private final UserRepository userRepository;
	
	private final ProductRepository productRepository;
	
	
	/* �닾�옄�븯湲� 
	 * REPEATABLE READ
	 * */
	@Override
	public InvestDto investProduct(long userId, long productId, long investAmount) throws Exception {
		User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(CustomErrorCode.CAN_NOT_FIND_USER_ID));
		
		Product product = productRepository.findByTimeAndProductId(LocalDateTime.now(),productId).orElseThrow(() -> new CustomException(CustomErrorCode.CAN_NOT_FIND_PRODUCT_ID));
		
		InvestInfo userInvestinfo = investInfoRepository.findByUserIdAndProductId(userId,productId)
												.orElseGet(() -> new InvestInfo());
																				
		long totalAmount = product.getCurrentInvestingAmount();
		long maxAmount = product.getTotalInvestingAmount();
		
		//InvestInfo investInfo = new InvestInfo();
		userInvestinfo.setProduct(Optional.ofNullable(userInvestinfo.getProduct()).orElseGet(()->product));
		userInvestinfo.setUser(Optional.ofNullable(userInvestinfo.getUser()).orElseGet(()->user));
						
		userInvestinfo.setInvestAmount(Optional.ofNullable(userInvestinfo.getInvestAmount()).orElseGet(()->0L));
		userInvestinfo.setInvestTime(Optional.ofNullable(userInvestinfo.getInvestTime()).orElseGet(()->LocalDateTime.now()));

		InvestLog investlog = new InvestLog();
		investlog.setUser(user);
		investlog.setProduct(product);
		investlog.setInvestAmount(investAmount);
		investlog.setBigo(investAmount + "투자시도");
		investlog.setInvestFlagCd("I");
		investlog.setInvestTime(LocalDateTime.now());
		
		if(maxAmount >= totalAmount + investAmount && !product.getStatus().equals(ProductStatus.CLOSE)) {
			
			investlog.setInvestInfo(userInvestinfo);
			product.setCurrentInvestingAmount(totalAmount + investAmount);
			userInvestinfo.setInvestAmount(userInvestinfo.getInvestAmount() + investAmount);
			
			investlog.setInvestResultStatusCd(InvestLogResultStatus.SUCCESS);
			
			if(!Optional.ofNullable(userInvestinfo.getCreateDate()).isPresent()) {
				product.setCurrentUserCnt(product.getCurrentUserCnt() + 1);
			}
			
			if(maxAmount == totalAmount + investAmount) {
				product.setStatus(ProductStatus.CLOSE);		//invest_status: Sold Out
			}
			
			productRepository.save(product);	//save or udpate
			investInfoRepository.save(userInvestinfo);
			investLogRepository.save(investlog);
			
			return new InvestDto(SUCCESS);
		}
		else if(product.getStatus().equals(ProductStatus.CLOSE)){
			investlog.setInvestResultStatusCd(InvestLogResultStatus.FAIL);
			investlog.setBigo("[" + CustomErrorCode.CAN_NOT_INVEST_SOLDOUT_PRODUCT + "]" + investAmount + "원 입금 시도 실패 !");
			investLogRepository.save(investlog);
			return new InvestDto(SOLD_OUT);
		}
		else {
			investlog.setInvestResultStatusCd(InvestLogResultStatus.FAIL);
			investlog.setBigo("[" + CustomErrorCode.CAN_NOT_OVER_MAX_AMOUNT + "]" + investAmount + "원 입금 시도 실패 !");
			investLogRepository.save(investlog);
			return new InvestDto(CAN_NOT_OVER_MAX_AMOUNT);
		}
	}
}
