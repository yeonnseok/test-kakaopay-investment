package com.test.kakaopay.investment.user.domain

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun existsByName(name: String): Boolean
}
