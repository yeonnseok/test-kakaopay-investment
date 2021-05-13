package com.test.kakaopay.investment.exceptions

class InvestingAmountExceedException(
    override val message: String = "모집금액을 초과하여 투자할 수 없습니다."
) : BadRequestException()
