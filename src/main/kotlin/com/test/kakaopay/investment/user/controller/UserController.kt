package com.test.kakaopay.investment.user.controller

import com.test.kakaopay.investment.common.ApiResponse
import com.test.kakaopay.investment.user.domain.UserService
import com.test.kakaopay.investment.user.domain.dto.UserCreateRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService
) {
    @PostMapping
    fun create(@RequestBody request: UserCreateRequest): ResponseEntity<ApiResponse> {
        val response = userService.create(request)
        return ResponseEntity.created(URI("/api/v1/users/${response.userId}"))
            .body(ApiResponse(
                statusCode = HttpStatus.CREATED.value(),
                data = response
            ))
    }
}
