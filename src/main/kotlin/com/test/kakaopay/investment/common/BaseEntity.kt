package com.test.kakaopay.investment.common

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@EntityListeners(value = [AuditingEntityListener::class])
@MappedSuperclass
abstract class BaseEntity{

    @CreatedDate
    @Column(nullable = false, updatable = false,  columnDefinition = "timestamp")
    var createdAt: LocalDateTime? = null

    @LastModifiedDate
    @Column(columnDefinition = "timestamp")
    var updatedAt  : LocalDateTime = LocalDateTime.now()
}
