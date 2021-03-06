package com.test.kakaopay.investment.investments.controller

import com.test.kakaopay.investment.investments.domain.Investment
import com.test.kakaopay.investment.investments.domain.InvestmentRepository
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigDecimal
import java.time.LocalDateTime

class InvestmentControllerTest : ControllerTest() {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var productRepository: ProductRepository

    @Autowired
    private lateinit var investmentRepository: InvestmentRepository

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
                title = "????????? ???????????????",
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
    fun `?????? ?????? API`() {
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
                        headerWithName("X-USER-ID").description("?????? ?????? ?????? (id)"),
                        headerWithName("Content-Type").description("????????? ??????")
                    ),
                    requestFields(
                        fieldWithPath("productId").description("?????? ?????? ID"),
                        fieldWithPath("investingAmount").description("?????? ??????")
                    ),
                    responseFields(
                        fieldWithPath("result").description("?????? ??????"),
                        fieldWithPath("statusCode").description("?????? ??????"),
                        fieldWithPath("data.investmentId").description("?????? ID"),
                        fieldWithPath("data.userId").description("????????? ID"),
                        fieldWithPath("data.productId").description("?????? ?????? ID"),
                        fieldWithPath("data.investingAmount").description("?????? ??????")
                    )
                )
            )
    }

    @Test
    fun `?????? ?????? ?????? ?????? API`() {
        // given
        val secondProduct = productRepository.save(
            Product(
                title = "?????? ???????????????",
                totalInvestingAmount = BigDecimal(10000000),
                currentInvestingAmount = BigDecimal(7000000),
                investorCount = 300,
                investingStatus = InvestingStatus.PROCEEDING,
                startedAt = LocalDateTime.of(2021, 3, 1, 0, 0, 0),
                finishedAt = LocalDateTime.of(2021, 12, 31, 0, 0, 0),
            )
        )

        investmentRepository.saveAll(
            listOf(
                Investment(
                    userId = userId!!,
                    productId = productId!!,
                    investingAmount = BigDecimal(50000)
                ),
                Investment(
                    userId = userId!!,
                    productId = secondProduct.id!!,
                    investingAmount = BigDecimal(100000)
                ),
                Investment(
                    userId = 2000,
                    productId = productId!!,
                    investingAmount = BigDecimal(150000)
                )
            )
        )

        // when
        val result = mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/investments")
                .header("X-USER-ID", userId!!)
        )

        // then
        result
            .andExpect(status().isOk)
            .andDo(
                document(
                    "investments/my-list",
                    requestHeaders(
                        headerWithName("X-USER-ID").description("?????? ?????? ?????? (id)")
                    ),
                    responseFields(
                        fieldWithPath("result").description("?????? ??????"),
                        fieldWithPath("statusCode").description("?????? ??????"),
                        fieldWithPath("data[].productId").description("?????? ?????? ID"),
                        fieldWithPath("data[].title").description("?????? ?????????"),
                        fieldWithPath("data[].totalInvestingAmount").description("??? ?????? ??????"),
                        fieldWithPath("data[].investingAmount").description("?????? ????????? ??????"),
                        fieldWithPath("data[].investedAt").description("?????? ??????")
                    )
                )
            )
    }
}
