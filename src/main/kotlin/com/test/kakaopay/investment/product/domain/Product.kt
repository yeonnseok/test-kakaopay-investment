package com.test.kakaopay.investment.product.domain

import com.test.kakaopay.investment.common.BaseEntity
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "product")
class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "title", nullable = false)
    var title: String,

    @Column(name = "total_investing_amount", nullable = false)
    var totalInvestingAmount: BigDecimal,

    @Column(name = "current_investing_amount", nullable = false)
    var currentInvestingAmount: BigDecimal = BigDecimal.ZERO,

    @Column(name = "investor_count", nullable = false)
    var investorCount: Long = 0,

    @Enumerated(EnumType.STRING)
    @Column(name = "investing_status", nullable = false)
    var investingStatus: InvestingStatus = InvestingStatus.PROCEEDING,

    @Column(name = "started_at")
    var startedAt: LocalDateTime,

    @Column(name = "finished_at")
    var finishedAt: LocalDateTime
) : BaseEntity()
