package com.test.kakaopay.investment.common.filter

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.filter.ShallowEtagHeaderFilter

@Configuration
class ETagHeaderFilter {
    @Bean
    fun shallowEtagHeaderFilter(): ShallowEtagHeaderFilter {
        return ShallowEtagHeaderFilter()
    }
}
