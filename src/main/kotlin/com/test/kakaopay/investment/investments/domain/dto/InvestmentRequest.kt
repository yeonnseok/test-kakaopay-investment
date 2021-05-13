package com.test.kakaopay.investment.investments.domain.dto

import java.math.BigDecimal
import javax.validation.constraints.Min

data class InvestmentRequest(
    val productId: Long,

    @Min(value = 1)
    val investingAmount: BigDecimal
)
