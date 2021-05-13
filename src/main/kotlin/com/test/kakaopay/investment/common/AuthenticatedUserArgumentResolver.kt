package com.test.kakaopay.investment.common

import com.test.kakaopay.investment.exceptions.UserNotFoundException
import com.test.kakaopay.investment.user.domain.User
import com.test.kakaopay.investment.user.domain.UserService
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class AuthenticatedUserArgumentResolver(
    private val userService: UserService
) : HandlerMethodArgumentResolver {

    companion object {
        private const val AUTHORIZATION_KEY = "X-USER-ID"
    }

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(AuthenticatedUser::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter, mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest, binderFactory: WebDataBinderFactory?
    ): User {
        val userId = webRequest.getHeader(AUTHORIZATION_KEY)!!.toLong()

        return try {
            userService.findById(userId)
        } catch (e: Exception) {
            throw UserNotFoundException()
        }
    }
}
