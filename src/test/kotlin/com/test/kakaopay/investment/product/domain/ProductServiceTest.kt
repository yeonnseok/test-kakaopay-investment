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
            title = "부동산 포트톨리오",
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
}
