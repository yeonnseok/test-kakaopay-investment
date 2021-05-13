package com.test.kakaopay.investment.product.domain

import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface ProductRepository : JpaRepository<Product, Long> {
    fun findByStartedAtLessThanEqualAndFinishedAtGreaterThanEqual(now: LocalDateTime, nowCopy: LocalDateTime): List<Product>
}
