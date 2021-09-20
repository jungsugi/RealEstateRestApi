package com.sooyoung.kakaopay.constants;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

public class Descriptor {

	public static FieldDescriptor[] getProductsResponseFields = new FieldDescriptor[] {
			fieldWithPath("[].product_id").type(JsonFieldType.NUMBER).optional().description("상품 아이디"),
			fieldWithPath("[].title").type(JsonFieldType.STRING).optional().description("상품 명"),
			fieldWithPath("[].total_investing_amount").type(JsonFieldType.NUMBER).optional().description("총 모집 금액"),
			fieldWithPath("[].current_invest_amount").type(JsonFieldType.NUMBER).optional().description("현재 모집 금액"),
			fieldWithPath("[].current_user_cnt").type(JsonFieldType.NUMBER).optional().description("현재 투자자 수"),
			fieldWithPath("[].status").type(JsonFieldType.STRING).optional().description("투자 상품 상태"),
			fieldWithPath("[].started_at").type(JsonFieldType.STRING).optional().description("투자 시작 일시"),
			fieldWithPath("[].finished_at").type(JsonFieldType.STRING).optional().optional().description("투자 종료 일시")
	};
	
	public static FieldDescriptor[] getMyProductsResponseFields = new FieldDescriptor[] {
			fieldWithPath("[].product_id").type(JsonFieldType.NUMBER).optional().description("상품 아이디"),
			fieldWithPath("[].title").type(JsonFieldType.STRING).optional().description("상품 명"),
			fieldWithPath("[].total_investing_amount").type(JsonFieldType.NUMBER).optional().description("총 모집 금액"),
			fieldWithPath("[].my_invest_amount").type(JsonFieldType.NUMBER).optional().description("나의 투자 금액"),
			fieldWithPath("[].invest_date").type(JsonFieldType.STRING).optional().description("투자 일시")
	};
	
	public static FieldDescriptor[] investProductResponseFields = new FieldDescriptor[] {
			fieldWithPath("message").type(JsonFieldType.STRING).description("투자 성공 여부 메시지")
	};
}
