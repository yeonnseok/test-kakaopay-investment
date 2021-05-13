package com.test.kakaopay.investment.exceptions

class UserNotFoundException(
    override val message: String = "유저를 찾을 수 없습니다."
) : RuntimeException()
