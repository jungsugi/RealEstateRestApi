package com.sooyoung.realestate.config;

import javax.servlet.http.HttpServletRequest;

import org.apache.coyote.ErrorState;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.sooyoung.realestate.controller.dto.UserDto;
import com.sooyoung.realestate.repository.UserRepository;
import com.sooyoung.realestate.service.user.UserServiceImpl;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
	
	private static final String X_USER_ID = "X-USER-ID";
	private final UserServiceImpl userService;
	
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
									NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
		Long userId = convertUserId(request.getHeader(X_USER_ID));
		
		if(!userService.isExists(userId)) {
			throw new CustomException(CustomErrorCode.CAN_NOT_FIND_USER_ID);
		}
		
		UserDto userDto = new UserDto();
		userDto.setId(userId);
		
		return userDto;
	}
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return UserDto.class.isAssignableFrom(parameter.getParameterType());
	}
	
	private Long convertUserId(String str) {
		Long userId = null;
		
		if(!StringUtils.hasText(str)) throw new CustomException(CustomErrorCode.INVAILD_USER_ID_REQUEST_HEADER);
		
		try {
			userId = Long.parseLong(str);
		}
		catch (NumberFormatException e) {
			throw new CustomException(CustomErrorCode.INVAILD_USER_ID_REQUEST_HEADER);
		}
		
		return userId;
	}
}
