package com.test.kakaopay.investment.investments.controller

import com.test.kakaopay.investment.common.ApiResponse
import com.test.kakaopay.investment.common.AuthenticatedUser
import com.test.kakaopay.investment.investments.domain.InvestmentService
import com.test.kakaopay.investment.investments.domain.dto.InvestmentRequest
import com.test.kakaopay.investment.user.domain.User
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/investments")
class InvestmentController(
    private val investmentService: InvestmentService
) {
    @PostMapping
    fun invest(
        @Valid @RequestBody request: InvestmentRequest,
        @AuthenticatedUser user: User
    ): ResponseEntity<ApiResponse> {
        val response = investmentService.invest(user.id!!, request)
        return ResponseEntity.created(URI("/api/v1/investments/${response.investmentId}"))
            .body(
                ApiResponse(
                    statusCode = HttpStatus.CREATED.value(),
                    data = response
                )
            )
    }

    @GetMapping
    fun myInvestment(@AuthenticatedUser user: User): ResponseEntity<ApiResponse> {
        val responses = investmentService.findByUserId(user.id!!)
        return ResponseEntity.ok(ApiResponse(data = responses))
    }
}
