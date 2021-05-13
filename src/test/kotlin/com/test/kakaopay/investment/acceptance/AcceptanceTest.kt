package com.test.kakaopay.investment.acceptance

import com.fasterxml.jackson.databind.ObjectMapper
import com.test.kakaopay.investment.common.ApiResponse
import com.test.kakaopay.investment.user.domain.dto.UserCreateRequest
import com.test.kakaopay.investment.user.domain.dto.UserCreateResponse
import io.restassured.RestAssured
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/truncate.sql")
class AcceptanceTest {

    @LocalServerPort
    protected var port: Int? = null

    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    protected var userId: Long? = null

    fun given(): RequestSpecification {
        return RestAssured.given().log().all()
    }

    @BeforeEach
    fun setUp() {
        if (RestAssured.port == RestAssured.UNDEFINED_PORT) {
            RestAssured.port = port!!
        }

        userId = createUser()
    }

    private fun createUser(): Long {
        val request = UserCreateRequest("kakao")

        val response = post("/api/v1/users", request, UserCreateResponse::class.java) as UserCreateResponse

        return response.userId
    }

    protected fun getResponseData(data: Any, classType: Class<*>): Any {
        val jsonData = objectMapper.writeValueAsString(data)
        return objectMapper.readValue(jsonData, classType)
    }

    protected fun post(path: String, request: Any, classType: Class<*>): Any {
        val apiResponse = given().
                    body(request).
                    contentType(MediaType.APPLICATION_JSON_VALUE).
                    accept(MediaType.APPLICATION_JSON_VALUE).
            `when`().
                    post(path).
            then().
                    log().all().
                    statusCode(HttpStatus.CREATED.value()).
                    contentType(MediaType.APPLICATION_JSON_VALUE).
                    extract().`as`(ApiResponse::class.java)
        return getResponseData(apiResponse.data, classType)
    }
}
