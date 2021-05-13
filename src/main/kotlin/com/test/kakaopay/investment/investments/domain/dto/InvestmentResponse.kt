package com.test.kakaopay.investment.investments.domain.dto

import com.test.kakaopay.investment.investments.domain.Investment
import com.test.kakaopay.investment.product.domain.Product
import java.math.BigDecimal

data class InvestmentResponse(
    val productId: Long,
    val title: String,
    val totalInvestingAmount: BigDecimal,
    val investingAmount: BigDecimal,
    val investedAt: String
) {
    companion object {
        fun of(investment: Investment, product: Product): InvestmentResponse {
            return InvestmentResponse(
                productId = investment.productId,
                title = product.title,
                totalInvestingAmount = product.totalInvestingAmount,
                investingAmount = investment.investingAmount,
                investedAt = investment.createdAt!!.toString()
            )
        }
    }
}
