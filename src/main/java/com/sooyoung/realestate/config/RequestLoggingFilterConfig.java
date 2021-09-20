package com.sooyoung.realestate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
public class RequestLoggingFilterConfig {

	/*
	 * Reuqest가 넘어올때, 특정 Request만 Fillter로 걸러서 Logging 할 수 있게 CommonsRequestLoggingFilter.class를 제공함.
	 * logback-spring.xml 에 logger태그에서 등록할 appendar 객체를 참조해서 사용
	 * 
	 * */
	@Bean
	public CommonsRequestLoggingFilter commonsRequestLoggingFilter () {
		
		CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
		filter.setIncludeQueryString(true);	//쿼리 문자열을 로그 메세지에 포함
		filter.setIncludePayload(true);		//request 내용을 로그에 포함
		filter.setMaxPayloadLength(10000);	//로그의 최대길이를 설정
		filter.setIncludeHeaders(true);		//헤더정보를 로그에 포함
		filter.setIncludeClientInfo(true);	//클라이언트 주소와 세션ID를 로그메세지에 포함
		return filter;
	}
	
	/*
	 * @Component : Spring IOC 컨테이너에서 생성과 함께 특정기능을 하는 객체로 관리하는 '스프링빈.
	 * @Configuration, @Bean : '자바빈' 을 SpringContiner에 담아주는 어노테이션
                                            (자바빈은 특정 형태의 클래스를 가리키는 뜻으로 사용, POJO(plain old java object) )
       그래서 Spring IOC 컨테이너에 Bean으로 등록하는 방법은 크게 3가지 가 있는것.
       - xml에 등록하는 방법
       - @Bean 어노테이션
       - @Component 어노테이션
       
       글고,
       @Bean과 @Configuration의 차이점은
       - @Bean : 외부라이브러리를 Bean으로 등록할때 사용. (Class단위가 아닌 Method 단위)
       - @Component : Class단위로 직접 컨트롤 가능한 객체에 사용됨.
	 * 
	 * */
	
}
