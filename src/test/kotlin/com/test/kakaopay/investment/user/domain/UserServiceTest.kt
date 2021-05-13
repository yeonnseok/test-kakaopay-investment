package com.test.kakaopay.investment.user.domain

import com.test.kakaopay.investment.exceptions.UserNameDuplicatedException
import com.test.kakaopay.investment.user.domain.dto.UserCreateRequest
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.shouldThrow
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql

@SpringBootTest
@Sql("/truncate.sql")
internal class UserServiceTest {

    @Autowired
    private lateinit var sut: UserService

    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    fun `유저 생성 실패`() {
        // given
        userRepository.save(User(name = "kakao"))
        val request = UserCreateRequest("kakao")

        // when
        val error = shouldThrow<UserNameDuplicatedException> { sut.create(request) }

        // then
        error.message shouldBe "이미 존재하는 이름입니다."
    }

    @Test
    fun `유저 생성`() {
        // given
        val request = UserCreateRequest("kakao")

        // when
        val response = sut.create(request)

        // then
        response.userId shouldNotBe 1
    }
}
