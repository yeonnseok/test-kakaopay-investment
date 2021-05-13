package com.test.kakaopay.investment.acceptance

import com.test.kakaopay.investment.product.domain.dto.ProductCreateRequest
import com.test.kakaopay.investment.product.domain.dto.ProductCreateResponse
import com.test.kakaopay.investment.user.domain.RoleType
import com.test.kakaopay.investment.user.domain.dto.UserCreateRequest
import io.kotlintest.shouldBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.stream.Stream

internal class ProductAcceptanceTest : AcceptanceTest() {

    @DisplayName("투자 상품 관리")
    @TestFactory
    fun manageProduct(): Stream<DynamicTest> {
        return Stream.of(
            DynamicTest.dynamicTest("투자 상품 생성", {
                // given
                val request = ProductCreateRequest(
                    title = "부동산 포트톨리오",
                    totalInvestingAmount = BigDecimal(5000000),
                    startedAt = LocalDateTime.of(2021, 3, 2, 0, 0, 0),
                    finishedAt = LocalDateTime.of(2021, 3, 9, 0, 0, 0)
                )
                val adminId = createUser(UserCreateRequest("admin", RoleType.ROLE_ADMIN))

                // when
                val response = post("/api/v1/products", adminId, request, ProductCreateResponse::class.java) as ProductCreateResponse

                // then
                response.productId shouldBe 1
            })
        )
    }
}