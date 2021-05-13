package com.test.kakaopay.investment.product.controller

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
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigDecimal
import java.time.LocalDateTime

class ProductControllerTest : ControllerTest() {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var productRepository: ProductRepository

    private var userId: Long? = null

    @BeforeEach
    fun setUp() {
        val user = userRepository.save(
            User(
                name = "kakao",
                role = RoleType.ROLE_USER
            )
        )
        userId = user.id!!
    }

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
            "title" to "부동산 포트폴리오",
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
                    responseFields(
                        fieldWithPath("result").description("응답 결과"),
                        fieldWithPath("statusCode").description("상태 코드"),
                        fieldWithPath("data.productId").description("투자 상품 ID")
                    )
                )
            )
    }

    @Test
    fun `투자 상품 목록 조회 API`() {
        // given
        productRepository.saveAll(listOf(
            Product(
                title = "부동산 포트폴리오",
                totalInvestingAmount = BigDecimal(5000000),
                currentInvestingAmount = BigDecimal(5000000),
                investorCount = 300,
                investingStatus = InvestingStatus.COMPLETED,
                startedAt = LocalDateTime.of(2021, 3, 1, 0, 0, 0),
                finishedAt = LocalDateTime.of(2021, 6, 15, 0, 0, 0),
            ),
            Product(
                title = "신용 포트폴리오",
                totalInvestingAmount = BigDecimal(10000000),
                currentInvestingAmount = BigDecimal(6000000),
                investorCount = 500,
                startedAt = LocalDateTime.of(2021, 3, 1, 0, 0, 0),
                finishedAt = LocalDateTime.of(2021, 12, 31, 0, 0, 0),
            )
        ))

        // when
        val result = mockMvc.perform(
            get("/api/v1/products")
                .header("X-USER-ID", userId!!)
        )

        // then
        result
            .andExpect(status().isOk)
            .andDo(
                document(
                    "products/find-list",
                    requestHeaders(
                        headerWithName("X-USER-ID").description("유저 식별 정보 (id)")
                    ),
                    responseFields(
                        fieldWithPath("result").description("응답 결과"),
                        fieldWithPath("statusCode").description("상태 코드"),
                        fieldWithPath("data[].productId").description("투자 상품 ID"),
                        fieldWithPath("data[].title").description("투자 상품명"),
                        fieldWithPath("data[].totalInvestingAmount").description("총 모집 금액"),
                        fieldWithPath("data[].currentInvestingAmount").description("현재 모집된 금액"),
                        fieldWithPath("data[].investorCount").description("총 투자자 수"),
                        fieldWithPath("data[].investingStatus").description("투자 모집 상태"),
                        fieldWithPath("data[].startedAt").description("모집 시작 일시"),
                        fieldWithPath("data[].finishedAt").description("모집 종료 일시"),
                    )
                )
            )
    }
}