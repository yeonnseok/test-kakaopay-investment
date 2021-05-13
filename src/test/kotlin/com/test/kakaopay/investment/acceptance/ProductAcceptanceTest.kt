package com.test.kakaopay.investment.acceptance

import com.google.gson.reflect.TypeToken
import com.test.kakaopay.investment.product.domain.InvestingStatus
import com.test.kakaopay.investment.product.domain.dto.ProductCreateRequest
import com.test.kakaopay.investment.product.domain.dto.ProductCreateResponse
import com.test.kakaopay.investment.product.domain.dto.ProductResponse
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
                    title = "부동산 포트폴리오",
                    totalInvestingAmount = BigDecimal(5000000),
                    startedAt = LocalDateTime.of(2021, 3, 1, 0, 0, 0),
                    finishedAt = LocalDateTime.of(2021, 12, 31, 0, 0, 0)
                )
                val adminId = createUser(UserCreateRequest("admin", RoleType.ROLE_ADMIN))

                // when
                val response = post("/api/v1/products", adminId, request, ProductCreateResponse::class.java) as ProductCreateResponse

                // then
                response.productId shouldBe 1
            }),

            DynamicTest.dynamicTest("투자 상품 목록 조회", {
                // when
                val type = object : TypeToken<List<ProductResponse>>() {}.type
                val responses = getList("/api/v1/products", type) as List<ProductResponse>

                // then
                responses[0].title shouldBe "부동산 포트폴리오"
                responses[0].investingStatus shouldBe InvestingStatus.PROCEEDING.korean
            })
        )
    }
}