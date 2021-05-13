package com.test.kakaopay.investment.investments.domain

import org.springframework.data.jpa.repository.JpaRepository

interface InvestmentRepository : JpaRepository<Investment, Long> {
    fun findByUserId(userId: Long): List<Investment>
}
