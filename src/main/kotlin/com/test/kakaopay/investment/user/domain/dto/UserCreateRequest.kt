package com.test.kakaopay.investment.user.domain.dto

import com.test.kakaopay.investment.user.domain.RoleType
import com.test.kakaopay.investment.user.domain.User

data class UserCreateRequest(
    val name: String,
    val role: RoleType = RoleType.ROLE_USER
) {
    fun toEntity(): User {
        return User(
            name = name,
            role = role
        )
    }
}
