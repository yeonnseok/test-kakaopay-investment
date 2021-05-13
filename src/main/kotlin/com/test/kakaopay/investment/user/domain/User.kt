package com.test.kakaopay.investment.user.domain

import com.test.kakaopay.investment.common.BaseEntity
import javax.persistence.*

@Entity
@Table(name = "user")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "name", nullable = false)
    var name: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    var role: RoleType
): BaseEntity()
