package com.test.kakaopay.investment.exceptions

import java.lang.RuntimeException

class ProductNotFoundException(
    override val message: String = "투자 상품을 찾을 수 없습니다."
) : RuntimeException()
