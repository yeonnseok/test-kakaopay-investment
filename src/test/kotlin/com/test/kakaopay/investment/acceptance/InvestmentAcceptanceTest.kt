package com.test.kakaopay.investment.acceptance

import com.google.gson.reflect.TypeToken
import com.test.kakaopay.investment.exceptions.ProductNotFoundException
import com.test.kakaopay.investment.investments.domain.Investment
import com.test.kakaopay.investment.investments.domain.InvestmentRepository
import com.test.kakaopay.investment.investments.domain.dto.InvestSuccessResponse
import com.test.kakaopay.investment.investments.domain.dto.InvestmentRequest
import com.test.kakaopay.investment.investments.domain.dto.InvestmentResponse
import com.test.kakaopay.investment.product.domain.InvestingStatus
import com.test.kakaopay.investment.product.domain.Product
import com.test.kakaopay.investment.product.domain.ProductRepository
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.stream.Stream

class InvestmentAcceptanceTest : AcceptanceTest() {

    @Autowired
    private lateinit var productRepository: ProductRepository

    @Autowired
    private lateinit var investmentRepository: InvestmentRepository

    private var product: Product? = null

    @BeforeEach
    fun setUp2() {
        product = productRepository.save(
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
    }

    @DisplayName("투자 관리")
    @TestFactory
    fun manageInvestment(): Stream<DynamicTest> {
        return Stream.of(
            DynamicTest.dynamicTest("투자 하기", {
                // given
                val request = InvestmentRequest(
                    productId = product!!.id!!,
                    investingAmount = BigDecimal(100000)
                )

                // when
                val response = post("/api/v1/investments", userId, request, InvestSuccessResponse::class.java) as InvestSuccessResponse

                // then
                response.investmentId shouldNotBe null
                response.userId shouldBe userId
                response.productId shouldBe product!!.id
                response.investingAmount shouldBe BigDecimal(100000)

                val product = productRepository.findById(product!!.id!!)
                    .orElseThrow { ProductNotFoundException() }

                product.investorCount shouldBe 11
                product.currentInvestingAmount shouldBe BigDecimal(2100000)
                product.investingStatus shouldBe InvestingStatus.PROCEEDING
            }),

            DynamicTest.dynamicTest("나의 투자 상품 조회", {
                // given
                val investment = investmentRepository.save(
                    Investment(
                        userId = userId!!,
                        productId = product!!.id!!,
                        investingAmount = BigDecimal(50000)
                    )
                )

                // when
                val type = object : TypeToken<List<InvestmentResponse>>() {}.type
                val responses = getList("/api/v1/investments", type) as List<InvestmentResponse>

                // then
                responses.size shouldBe 2
                responses[1].productId shouldBe product!!.id
                responses[1].title shouldBe product!!.title
                responses[1].totalInvestingAmount shouldBe product!!.totalInvestingAmount
                responses[1].investingAmount shouldBe BigDecimal(50000)
                responses[1].investedAt shouldBe investment.createdAt.toString()
            })
        )
    }
}
