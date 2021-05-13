package com.test.kakaopay.investment.product.domain.dto

import com.test.kakaopay.investment.product.domain.InvestingStatus
import java.math.BigDecimal
import java.time.LocalDateTime

data class ProductResponse(
    val productId: Long,
    val title: String,
    val totalInvestingAmount: BigDecimal,
    val currentInvestingAmount: BigDecimal,
    val investorCount: Long,
    val investingStatus: InvestingStatus,
    val startedAt: LocalDateTime,
    val finishedAt: LocalDateTime
)
