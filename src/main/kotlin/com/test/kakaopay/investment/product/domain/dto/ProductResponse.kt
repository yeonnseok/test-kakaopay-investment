package com.test.kakaopay.investment.product.domain.dto

import com.test.kakaopay.investment.product.domain.Product
import java.math.BigDecimal

data class ProductResponse(
    val productId: Long,
    val title: String,
    val totalInvestingAmount: BigDecimal,
    val currentInvestingAmount: BigDecimal,
    val investorCount: Long,
    val investingStatus: String,
    val startedAt: String,
    val finishedAt: String
) {
    companion object {
        fun of(product: Product): ProductResponse {
            return ProductResponse(
                productId = product.id!!,
                title = product.title,
                totalInvestingAmount = product.totalInvestingAmount,
                currentInvestingAmount = product.currentInvestingAmount,
                investorCount = product.investorCount,
                investingStatus = product.investingStatus.korean,
                startedAt = product.startedAt.toString(),
                finishedAt = product.finishedAt.toString(),
            )
        }
    }
}
