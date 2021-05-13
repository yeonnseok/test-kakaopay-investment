package com.test.kakaopay.investment.product.controller

import com.test.kakaopay.investment.restdocs.ControllerTest
import com.test.kakaopay.investment.user.domain.RoleType
import com.test.kakaopay.investment.user.domain.User
import com.test.kakaopay.investment.user.domain.UserRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigDecimal
import java.time.LocalDateTime

class ProductControllerTest : ControllerTest() {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    fun `투자 상품 생성 API`() {
        // given
        val user = userRepository.save(
            User(
                name = "admin",
                role = RoleType.ROLE_ADMIN
            )
        )
        val body = mapOf(
            "title" to "부동산 포트톨리오",
            "totalInvestingAmount" to BigDecimal(5000000),
            "startedAt" to LocalDateTime.of(2021, 3, 2, 0, 0, 0),
            "finishedAt" to LocalDateTime.of(2021, 3, 9, 0, 0, 0)
        )

        // when
        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/products")
                .header("X-USER-ID", user.id!!)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsBytes(body))
        )

        // then
        result
            .andExpect(status().isCreated)
            .andDo(
                document(
                    "products/create",
                    requestHeaders(
                        headerWithName("X-USER-ID").description("유저 식별 정보 (id)"),
                        headerWithName("Content-Type").description("데이터 타입")
                    ),
                    requestFields(
                        fieldWithPath("title").description("투자 상품 이름"),
                        fieldWithPath("totalInvestingAmount").description("총 모집 금액"),
                        fieldWithPath("startedAt").description("모집 시작 일시"),
                        fieldWithPath("finishedAt").description("모집 종료 일시")
                    ),
                    PayloadDocumentation.responseFields(
                        fieldWithPath("result").description("응답 결과"),
                        fieldWithPath("statusCode").description("상태 코드"),
                        fieldWithPath("data.productId").description("투자 상품 ID")
                    )
                )
            )
    }
}