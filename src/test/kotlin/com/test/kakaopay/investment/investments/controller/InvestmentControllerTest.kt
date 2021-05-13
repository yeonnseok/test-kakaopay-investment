package com.test.kakaopay.investment.investments.controller

import com.test.kakaopay.investment.product.domain.InvestingStatus
import com.test.kakaopay.investment.product.domain.Product
import com.test.kakaopay.investment.product.domain.ProductRepository
import com.test.kakaopay.investment.restdocs.ControllerTest
import com.test.kakaopay.investment.user.domain.RoleType
import com.test.kakaopay.investment.user.domain.User
import com.test.kakaopay.investment.user.domain.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigDecimal
import java.time.LocalDateTime

class InvestmentControllerTest : ControllerTest() {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var productRepository: ProductRepository

    private var userId: Long? = null

    private var productId: Long? = null

    @BeforeEach
    fun setUp() {
        val user = userRepository.save(
            User(
                name = "kakao",
                role = RoleType.ROLE_USER
            )
        )
        userId = user.id

        val product = productRepository.save(
            Product(
                title = "부동산 포트폴리오",
                totalInvestingAmount = BigDecimal(5000000),
                currentInvestingAmount = BigDecimal(2000000),
                investorCount = 10,
                investingStatus = InvestingStatus.PROCEEDING,
                startedAt = LocalDateTime.of(2021, 3, 1, 0, 0, 0),
                finishedAt = LocalDateTime.of(2021, 12, 31, 0, 0, 0),
            )
        )
        productId = product.id
    }

    @Test
    fun `투자 하기 API`() {
        // given
        val body = mapOf(
            "productId" to productId,
            "investingAmount" to BigDecimal(100000)
        )

        // when
        val result = mockMvc.perform(
            post("/api/v1/investments")
                .header("X-USER-ID", userId!!)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsBytes(body))
        )

        // then
        result
            .andExpect(status().isCreated)
            .andDo(
                document(
                    "investments/create",
                    requestHeaders(
                        headerWithName("X-USER-ID").description("유저 식별 정보 (id)"),
                        headerWithName("Content-Type").description("데이터 타입")
                    ),
                    requestFields(
                        fieldWithPath("productId").description("투자 상품 ID"),
                        fieldWithPath("investingAmount").description("투자 금액")
                    ),
                    responseFields(
                        fieldWithPath("result").description("응답 결과"),
                        fieldWithPath("statusCode").description("상태 코드"),
                        fieldWithPath("data.investmentId").description("투자 ID"),
                        fieldWithPath("data.userId").description("사용자 ID"),
                        fieldWithPath("data.productId").description("투자 상품 ID"),
                        fieldWithPath("data.investingAmount").description("투자 금액")
                    )
                )
            )
    }
}