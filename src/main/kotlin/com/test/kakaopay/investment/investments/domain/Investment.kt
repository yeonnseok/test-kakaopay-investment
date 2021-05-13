package com.test.kakaopay.investment.investments.domain

import com.test.kakaopay.investment.common.BaseEntity
import java.math.BigDecimal
import javax.persistence.*

@Entity
@Table(name = "investment")
class Investment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "user_id", nullable = false)
    var userId: Long,

    @Column(name = "product_id", nullable = false)
    var productId: Long,

    @Column(name = "investing_amount", nullable = false)
    var investingAmount: BigDecimal
): BaseEntity()
