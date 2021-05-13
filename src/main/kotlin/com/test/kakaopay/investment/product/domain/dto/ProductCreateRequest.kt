package com.test.kakaopay.investment.product.domain.dto

import com.test.kakaopay.investment.product.domain.Product
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.validation.constraints.Min
import javax.validation.constraints.Size

data class ProductCreateRequest(
    @Size(min = 2)
    val title: String,

    @Min(value = 1)
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
