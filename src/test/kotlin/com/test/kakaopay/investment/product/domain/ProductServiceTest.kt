package com.test.kakaopay.investment.product.domain

import com.test.kakaopay.investment.exceptions.AuthorizationException
import com.test.kakaopay.investment.product.domain.dto.ProductCreateRequest
import com.test.kakaopay.investment.user.domain.RoleType
import com.test.kakaopay.investment.user.domain.User
import com.test.kakaopay.investment.user.domain.UserRepository
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import java.math.BigDecimal
import java.time.LocalDateTime

@SpringBootTest
@Sql("/truncate.sql")
internal class ProductServiceTest {

    @Autowired
    private lateinit var sut: ProductService

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var productRepository: ProductRepository

    @Test
    fun `투자 상품 생성 실패 - 권한 부족`() {
        // given
        val user = userRepository.save(
            User(
                name = "kakao",
                role = RoleType.ROLE_USER
            )
        )
        val request = ProductCreateRequest(
            title = "부동산 포트폴리오",
            totalInvestingAmount = BigDecimal(5000000),
            startedAt = LocalDateTime.of(2021, 3, 2, 0, 0, 0),
            finishedAt = LocalDateTime.of(2021, 3, 9, 0, 0, 0)
        )

        // when
        val error = shouldThrow<AuthorizationException> { sut.create(request, user.id!!) }

        // then
        error.message shouldBe "권한이 없습니다."
    }

    @Test
    fun `투자 상품 생성`() {
        // given
        val user = userRepository.save(
            User(
                name = "admin",
                role = RoleType.ROLE_ADMIN
            )
        )
        val request = ProductCreateRequest(
            title = "부동산 포트톨리오",
            totalInvestingAmount = BigDecimal(5000000),
            startedAt = LocalDateTime.of(2021, 3, 2, 0, 0, 0),
            finishedAt = LocalDateTime.of(2021, 3, 9, 0, 0, 0)
        )

        // when
        val response = sut.create(request, user.id!!)

        // then
        response.productId shouldBe 1
    }

    @Test
    fun `투자 상품 목록 조회`() {
        // given
        productRepository.saveAll(listOf(
            Product(
                title = "부동산 포트폴리오",
                totalInvestingAmount = BigDecimal(5000000),
                startedAt = LocalDateTime.of(2021, 3, 2, 0, 0, 0),
                finishedAt = LocalDateTime.of(2021, 3, 10, 0, 0, 0),
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
        val responses = sut.findList()

        // then
        responses.size shouldBe 1
        responses[0].title shouldBe "신용 포트폴리오"
        responses[0].currentInvestingAmount shouldBe BigDecimal(6000000)
        responses[0].investorCount shouldBe 500
        responses[0].investingStatus shouldBe InvestingStatus.PROCEEDING.korean
    }
}
