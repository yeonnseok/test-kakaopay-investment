package com.test.kakaopay.investment.product.controller

import com.test.kakaopay.investment.common.ApiResponse
import com.test.kakaopay.investment.common.AuthenticatedUser
import com.test.kakaopay.investment.product.domain.ProductService
import com.test.kakaopay.investment.product.domain.dto.ProductCreateRequest
import com.test.kakaopay.investment.user.domain.User
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/api/v1/products")
class ProductController(
    private val productService: ProductService
) {
    @PostMapping
    fun create(
        @RequestBody request: ProductCreateRequest,
        @AuthenticatedUser user: User
    ): ResponseEntity<ApiResponse> {
        val response = productService.create(request, user.id!!)
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
