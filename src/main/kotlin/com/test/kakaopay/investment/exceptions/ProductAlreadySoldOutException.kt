package com.test.kakaopay.investment.exceptions

class ProductAlreadySoldOutException(
    override val message: String = "이미 모집 완료된 투자 상품입니다."
) : BadRequestException()
