package com.test.kakaopay.investment.user.domain.dto

import com.test.kakaopay.investment.user.domain.User

data class UserCreateRequest(
    val name: String
) {
    fun toEntity(): User {
        return User(name = name)
    }
}
