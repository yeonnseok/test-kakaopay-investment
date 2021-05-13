package com.test.kakaopay.investment.exceptions

import javax.naming.AuthenticationException

class AuthorizationException(
    override val message: String = "권한이 없습니다."
) : AuthenticationException()
