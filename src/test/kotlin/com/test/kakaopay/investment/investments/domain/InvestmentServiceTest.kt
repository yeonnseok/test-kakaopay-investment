package com.test.kakaopay.investment.investments.domain

import com.test.kakaopay.investment.exceptions.InvestingAmountExceedException
import com.test.kakaopay.investment.exceptions.NotValidInvestingPeriodException
import com.test.kakaopay.investment.exceptions.ProductAlreadySoldOutException
import com.test.kakaopay.investment.exceptions.ProductNotFoundException
import com.test.kakaopay.investment.investments.domain.dto.InvestmentRequest
import com.test.kakaopay.investment.product.domain.InvestingStatus
import com.test.kakaopay.investment.product.domain.Product
import com.test.kakaopay.investment.product.domain.ProductRepository
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal
import java.time.LocalDateTime

@SpringBootTest
internal class InvestmentServiceTest {

    @Autowired
    private lateinit var sut: InvestmentService

    @Autowired
    private lateinit var productRepository: ProductRepository

    private var productId: Long? = null

    @BeforeEach
    fun setUp() {
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
    fun `투자 실패 - 현재 모집 기간이 아닌 투자 상품`() {
        // given
        val product = productRepository.save(
            Product(
                title = "기간 지난 포트폴리오",
                totalInvestingAmount = BigDecimal(5000000),
                currentInvestingAmount = BigDecimal(2000000),
                investorCount = 10,
                investingStatus = InvestingStatus.PROCEEDING,
                startedAt = LocalDateTime.of(2021, 3, 1, 0, 0, 0),
                finishedAt = LocalDateTime.of(2021, 3, 31, 0, 0, 0),
            )
        )
        val request = InvestmentRequest(
            productId = product.id!!,
            investingAmount = BigDecimal(100000)
        )

        // when
        val error = shouldThrow<NotValidInvestingPeriodException> {
            sut.invest(1, request)
        }

        // then
        error.message shouldBe "현재 모집 기간이 아닌 투자 상품입니다."
    }

    @Test
    fun `투자 실패 - 이미 모집 완료된 투자 상품`() {
        // given
        val soldOutProduct = productRepository.save(
            Product(
                title = "신용 포트폴리오",
                totalInvestingAmount = BigDecimal(5000000),
                currentInvestingAmount = BigDecimal(5000000),
                investorCount = 10,
                investingStatus = InvestingStatus.SOLD_OUT,
                startedAt = LocalDateTime.of(2021, 3, 1, 0, 0, 0),
                finishedAt = LocalDateTime.of(2021, 12, 31, 0, 0, 0),
            )
        )
        val request = InvestmentRequest(
            productId = soldOutProduct.id!!,
            investingAmount = BigDecimal(100000)
        )

        // when
        val error = shouldThrow<ProductAlreadySoldOutException> {
            sut.invest(1, request)
        }

        // then
        error.message shouldBe "이미 모집 완료된 투자 상품입니다."
    }

    @Test
    fun `투자 실패 - 총 모집 금액을 초과하는 경우`() {
        // given
        val request = InvestmentRequest(
            productId = productId!!,
            investingAmount = BigDecimal(10000000)
        )

        // when
        val error = shouldThrow<InvestingAmountExceedException> {
            sut.invest(1, request)
        }

        // then
        error.message shouldBe "모집금액을 초과하여 투자할 수 없습니다."
    }

    @Test
    fun `투자 성공`() {
        // given
        val request = InvestmentRequest(
            productId = productId!!,
            investingAmount = BigDecimal(3000000)
        )

        // when
        val response = sut.invest(1, request)

        // then
        response.userId shouldBe 1
        response.productId shouldBe productId
        response.investingAmount shouldBe BigDecimal(3000000)

        val product = productRepository.findById(productId!!)
            .orElseThrow { ProductNotFoundException() }

        product.investorCount shouldBe 11
        product.currentInvestingAmount shouldBe BigDecimal(5000000)
        product.investingStatus shouldBe InvestingStatus.SOLD_OUT
    }
}
