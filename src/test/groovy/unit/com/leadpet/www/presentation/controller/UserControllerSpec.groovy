package com.leadpet.www.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.leadpet.www.infrastructure.domain.users.LoginMethod
import com.leadpet.www.presentation.dto.request.SignUpUserRequestDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
class UserControllerSpec extends Specification {
    private final String USER_URL = "/v1/user"

    @Autowired
    WebApplicationContext webApplicationContext
    MockMvc mvc

    def setup() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    def "회원가입 validation: #testCase"() throws Exception {
        setup:
        def signUpUserRequestDto = SignUpUserRequestDto.builder()
                .loginMethod(loginMethod)
                .uid(uid)
                .email(email)
                .password(password)
                .profileImage(profileImage)
                .name(name)
                .build();

        expect:
        mvc.perform(post(USER_URL + "/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(signUpUserRequestDto)))
                .andExpect(status().isBadRequest());

        where:
        testCase                    | loginMethod        | uid         | email            | password   | profileImage | name
        "KAKAO: loginMethod가 null"  | null               | "kakaoUid"  | null             | null       | null         | "kakao"
        "KAKAO: uid가 null"          | LoginMethod.KAKAO  | null        | null             | null       | null         | "kakao"
        "KAKAO: name이 null"         | LoginMethod.KAKAO  | "kakaoUid"  | null             | null       | null         | null
        "GOOGLE: loginMethod가 null" | null               | "googleUid" | null             | null       | null         | "google"
        "GOOGLE: uid가 null"         | LoginMethod.GOOGLE | null        | null             | null       | null         | "google"
        "GOOGLE: name이 null"        | LoginMethod.GOOGLE | "googleUid" | null             | null       | null         | null
        "APPLE: loginMethod가 null"  | null               | "appleUid"  | null             | null       | null         | "apple"
        "APPLE: uid가 null"          | LoginMethod.APPLE  | null        | null             | null       | null         | "apple"
        "APPLE: name이 null"         | LoginMethod.APPLE  | "appleUid"  | null             | null       | null         | null
        "EMAIL: loginMethod가 null"  | null               | "emailUid"  | "test@gmail.com" | "password" | null         | "email"
        "EMAIL: uid가 null"          | LoginMethod.EMAIL  | null        | "test@gmail.com" | "password" | null         | "email"
        "EMAIL: name이 null"         | LoginMethod.EMAIL  | "emailUid"  | "test@gmail.com" | "password" | null         | null
        "EMAIL: email이 null"        | LoginMethod.EMAIL  | "emailUid"  | null             | "password" | null         | "email"
        "EMAIL: password가 null"     | LoginMethod.EMAIL  | "emailUid"  | "test@gmail.com" | null       | null         | "email"
    }
}
