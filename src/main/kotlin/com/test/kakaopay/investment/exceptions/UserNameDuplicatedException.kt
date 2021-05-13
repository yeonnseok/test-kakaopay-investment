package com.test.kakaopay.investment.exceptions

class UserNameDuplicatedException(
    override val message: String = "이미 존재하는 이름입니다."
) : BadRequestException()
