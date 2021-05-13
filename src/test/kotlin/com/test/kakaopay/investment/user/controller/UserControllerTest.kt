package com.test.kakaopay.investment.user.controller

import com.test.kakaopay.investment.restdocs.ControllerTest
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

internal class UserControllerTest : ControllerTest() {

    @Test
    fun `유저 생성 API`() {
        // given
        val body = mapOf(
            "name" to "kakao"
        )

        // when
        val result = mockMvc.perform(
            post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsBytes(body))
        )

        // then
        result
            .andExpect(status().isCreated)
            .andDo(
                document(
                    "users/create",
                    requestHeaders(
                        headerWithName("Content-Type").description("데이터 타입")
                    ),
                    requestFields(
                        fieldWithPath("name")
                            .description("유저 이름")
                    ),
                    PayloadDocumentation.responseFields(
                        fieldWithPath("result").description("응답 결과"),
                        fieldWithPath("statusCode").description("상태 코드"),
                        fieldWithPath("data.userId").description("유저 ID")
                    )
                )
            )
    }
}
