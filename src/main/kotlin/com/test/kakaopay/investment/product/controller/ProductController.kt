package com.test.kakaopay.investment.product.controller

import com.test.kakaopay.investment.common.ApiResponse
import com.test.kakaopay.investment.product.domain.ProductService
import com.test.kakaopay.investment.product.domain.dto.ProductCreateRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/v1/products")
class ProductController(
    private val productService: ProductService
) {
    @PostMapping
    fun create(
        @RequestBody request: ProductCreateRequest,
        servletRequest: HttpServletRequest
    ): ResponseEntity<ApiResponse> {
        val userId = servletRequest.getHeader("X-USER-ID").toLong()
        val response = productService.create(request, userId)
        return ResponseEntity.created(URI("/api/v1/products/${response.productId}"))
            .body(
                ApiResponse(
                    statusCode = HttpStatus.CREATED.value(),
                    data = response
                )
            )
    }

    @GetMapping
    fun findList(): ResponseEntity<ApiResponse> {
        val response = productService.findList()
        return ResponseEntity
            .ok(
                ApiResponse(data = response)
            )
    }
}
