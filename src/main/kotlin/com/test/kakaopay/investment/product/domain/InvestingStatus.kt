package com.test.kakaopay.investment.product.domain

enum class InvestingStatus(
    val korean: String
) {
    PROCEEDING("모집 진행중"),
    SOLD_OUT("모집 완료");
}