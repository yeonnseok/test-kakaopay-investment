package com.test.kakaopay.investment.common.interceptor

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthInterceptor : HandlerInterceptor {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val noValidate = (handler as HandlerMethod).getMethodAnnotation(NoValidate::class.java)
        if (noValidate != null) {
            return true
        }

        val userId = request.getHeader("X-USER-ID")
        log.info("request user id : ${userId}")

        if (userId != null) {
            return true
        }

        return false
    }
}
