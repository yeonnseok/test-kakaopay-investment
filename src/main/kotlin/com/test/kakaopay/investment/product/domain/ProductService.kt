package com.test.kakaopay.investment.product.domain

import com.test.kakaopay.investment.exceptions.AuthorizationException
import com.test.kakaopay.investment.exceptions.UserNotFoundException
import com.test.kakaopay.investment.product.domain.dto.ProductCreateRequest
import com.test.kakaopay.investment.product.domain.dto.ProductCreateResponse
import com.test.kakaopay.investment.user.domain.RoleType
import com.test.kakaopay.investment.user.domain.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ProductService(
    private val userRepository: UserRepository,
    private val productRepository: ProductRepository
) {
    fun create(request: ProductCreateRequest, userId: Long): ProductCreateResponse {
        validateUser(userId)

        val product = productRepository.save(request.toEntity())
        return ProductCreateResponse(product.id!!)
    }

    private fun validateUser(userId: Long) {
        val user = userRepository.findById(userId)
            .orElseThrow { UserNotFoundException() }

        if (user.role == RoleType.ROLE_USER) {
            throw AuthorizationException()
        }
    }
}
