package com.test.kakaopay.investment.product.domain.dto

import com.test.kakaopay.investment.product.domain.Product
import java.math.BigDecimal
import java.time.LocalDateTime

data class ProductCreateRequest(
    val title: String,
    val totalInvestingAmount: BigDecimal,
    val startedAt: LocalDateTime,
    val finishedAt: LocalDateTime
) {
    fun toEntity(): Product {
        return Product(
            title = title,
            totalInvestingAmount = totalInvestingAmount,
            startedAt = startedAt,
            finishedAt = finishedAt
        )
    }
}
