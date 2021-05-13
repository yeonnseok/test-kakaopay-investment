package com.test.kakaopay.investment.investments.domain

import com.test.kakaopay.investment.exceptions.InvestingAmountExceedException
import com.test.kakaopay.investment.exceptions.NotValidInvestingPeriodException
import com.test.kakaopay.investment.exceptions.ProductAlreadySoldOutException
import com.test.kakaopay.investment.exceptions.ProductNotFoundException
import com.test.kakaopay.investment.investments.domain.dto.InvestSuccessResponse
import com.test.kakaopay.investment.investments.domain.dto.InvestmentRequest
import com.test.kakaopay.investment.product.domain.InvestingStatus
import com.test.kakaopay.investment.product.domain.Product
import com.test.kakaopay.investment.product.domain.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class InvestmentService(
    private val productRepository: ProductRepository,
    private val investmentRepository: InvestmentRepository
) {
    @Transactional
    fun invest(userId: Long, request: InvestmentRequest): InvestSuccessResponse {
        val product = productRepository.findById(request.productId)
            .orElseThrow { ProductNotFoundException() }

        validateProductInvestingPeriod(product.startedAt, product.finishedAt)
        validateProductInvestingStatus(product.investingStatus)
        validateProductInvestingAmount(product, request.investingAmount)

        updateProduct(product, request)
        val investment = investmentRepository.save(
            Investment(
                userId = userId,
                productId = product.id!!,
                investingAmount = request.investingAmount
            )
        )
        return InvestSuccessResponse.of(investment)
    }

    private fun validateProductInvestingPeriod(startedAt: LocalDateTime, finishedAt: LocalDateTime) {
        val now = LocalDateTime.now()
        if (startedAt > now || finishedAt < now) {
            throw NotValidInvestingPeriodException()
        }
    }

    private fun validateProductInvestingStatus(investingStatus: InvestingStatus) {
        if (investingStatus == InvestingStatus.SOLD_OUT) {
            throw ProductAlreadySoldOutException()
        }
    }

    private fun validateProductInvestingAmount(product: Product, investingAmount: BigDecimal) {
        if (product.currentInvestingAmount + investingAmount > product.totalInvestingAmount) {
            throw InvestingAmountExceedException()
        }
    }

    private fun updateProduct(product: Product, request: InvestmentRequest) {
        product.investorCount++
        product.currentInvestingAmount += request.investingAmount

        if (product.currentInvestingAmount == product.totalInvestingAmount) {
            product.investingStatus = InvestingStatus.SOLD_OUT
        }
    }
}
