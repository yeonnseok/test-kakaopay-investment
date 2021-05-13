package com.test.kakaopay.investment.exceptions

class NotValidInvestingPeriodException(
    override val message: String = "현재 모집 기간이 아닌 투자 상품입니다."
) : BadRequestException()
