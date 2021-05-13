package com.test.kakaopay.investment.investments.controller

import com.test.kakaopay.investment.common.ApiResponse
import com.test.kakaopay.investment.investments.domain.InvestmentService
import com.test.kakaopay.investment.investments.domain.dto.InvestmentRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/v1/investments")
class InvestmentController(
    private val investmentService: InvestmentService
) {
    @PostMapping
    fun invest(
        @RequestBody request: InvestmentRequest,
        servletRequest: HttpServletRequest
    ): ResponseEntity<ApiResponse> {
        val userId = servletRequest.getHeader("X-USER-ID").toLong()
        val response = investmentService.invest(userId, request)
        return ResponseEntity.created(URI("/api/v1/investments/${response.investmentId}"))
            .body(
                ApiResponse(
                    statusCode = HttpStatus.CREATED.value(),
                    data = response
                )
            )
    }
}
