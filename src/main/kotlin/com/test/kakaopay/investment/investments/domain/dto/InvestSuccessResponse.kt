package com.test.kakaopay.investment.investments.domain.dto

import com.test.kakaopay.investment.investments.domain.Investment
import java.math.BigDecimal

data class InvestSuccessResponse(
    val investmentId: Long,
    val userId: Long,
    val productId: Long,
    val investingAmount: BigDecimal
) {
    companion object {
        fun of(investment: Investment): InvestSuccessResponse {
            return InvestSuccessResponse(
                investmentId = investment.id!!,
                userId = investment.userId,
                productId = investment.productId,
                investingAmount = investment.investingAmount
            )
        }
    }
}
