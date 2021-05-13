package com.test.kakaopay.investment.user.domain

import com.test.kakaopay.investment.exceptions.UserNameDuplicatedException
import com.test.kakaopay.investment.exceptions.UserNotFoundException
import com.test.kakaopay.investment.user.domain.dto.UserCreateRequest
import com.test.kakaopay.investment.user.domain.dto.UserCreateResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository
) {
    @Transactional
    fun create(request: UserCreateRequest): UserCreateResponse {
        validateUserName(request.name)

        val user = userRepository.save(request.toEntity())
        return UserCreateResponse(
            userId = user.id!!
        )
    }

    private fun validateUserName(name: String) {
        if (userRepository.existsByName(name)) {
            throw UserNameDuplicatedException()
        }
    }

    fun findById(userId: Long): User {
        return userRepository.findById(userId)
            .orElseThrow { UserNotFoundException() }
    }
}
