package com.leadpet.www.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.leadpet.www.application.service.UserService
import com.leadpet.www.infrastructure.db.UsersRepository
import com.leadpet.www.infrastructure.domain.users.LoginMethod
import com.leadpet.www.infrastructure.domain.users.Users
import com.leadpet.www.presentation.dto.request.LogInRequestDto
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
    @Autowired
    UserService userService
    @Autowired
    UsersRepository usersRepository
    MockMvc mvc

    def setup() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    def cleanup() {
        usersRepository.deleteAll()
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
                .userType(userType)
                .shelterName(shelterName)
                .shelterPhoneNumber(shelterPhoneNumber)
                .shelterAddress(shelterAddress)
                .build();

        expect:
        mvc.perform(post(USER_URL + "/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(signUpUserRequestDto)))
                .andExpect(status().isBadRequest());

        where:
        testCase                       | loginMethod        | uid         | email            | password   | profileImage | name     | userType               | shelterName | shelterAddress | shelterPhoneNumber
        "KAKAO: loginMethod가 null"     | null               | "kakaoUid"  | null             | null       | null         | "kakao"  | Users.UserType.NORMAL  | null        | null           | null
        "KAKAO: uid가 null"             | LoginMethod.KAKAO  | null        | null             | null       | null         | "kakao"  | Users.UserType.NORMAL  | null        | null           | null
        "KAKAO: name이 null"            | LoginMethod.KAKAO  | "kakaoUid"  | null             | null       | null         | null     | Users.UserType.NORMAL  | null        | null           | null
        "KAKAO: userType이 null"        | LoginMethod.KAKAO  | "kakaoUid"  | null             | null       | null         | "kakao"  | null                   | null        | null           | null
        "KAKAO(보호소): 보호소 이름이 없는 경우"    | LoginMethod.KAKAO  | "kakaoUid"  | null             | null       | null         | "kakao"  | Users.UserType.SHELTER | null        | "보호소 주소"       | "01012345678"
        "KAKAO(보호소): 보호소 주소이 없는 경우"    | LoginMethod.KAKAO  | "kakaoUid"  | null             | null       | null         | "kakao"  | Users.UserType.SHELTER | "보호소 이름"    | null           | "01012345678"
        "KAKAO(보호소): 보호소 전화번호이 없는 경우"  | LoginMethod.KAKAO  | "kakaoUid"  | null             | null       | null         | "kakao"  | Users.UserType.SHELTER | "보호소 이름"    | "보호소 주소"       | null
        "GOOGLE: loginMethod가 null"    | null               | "googleUid" | null             | null       | null         | "google" | Users.UserType.NORMAL  | null        | null           | null
        "GOOGLE: uid가 null"            | LoginMethod.GOOGLE | null        | null             | null       | null         | "google" | Users.UserType.NORMAL  | null        | null           | null
        "GOOGLE: name이 null"           | LoginMethod.GOOGLE | "googleUid" | null             | null       | null         | null     | Users.UserType.NORMAL  | null        | null           | null
        "GOOGLE: userType이 null"       | LoginMethod.GOOGLE | "googleUid" | null             | null       | null         | "google" | null                   | null        | null           | null
        "GOOGLE(보호소): 보호소 이름이 없는 경우"   | LoginMethod.GOOGLE | "googleUid" | null             | null       | null         | "google" | Users.UserType.SHELTER | null        | "보호소 주소"       | "01012345678"
        "GOOGLE(보호소): 보호소 주소이 없는 경우"   | LoginMethod.GOOGLE | "googleUid" | null             | null       | null         | "google" | Users.UserType.SHELTER | "보호소 이름"    | null           | "01012345678"
        "GOOGLE(보호소): 보호소 전화번호이 없는 경우" | LoginMethod.GOOGLE | "googleUid" | null             | null       | null         | "google" | Users.UserType.SHELTER | "보호소 이름"    | "보호소 주소"       | null
        "APPLE: loginMethod가 null"     | null               | "appleUid"  | null             | null       | null         | "apple"  | Users.UserType.NORMAL  | null        | null           | null
        "APPLE: uid가 null"             | LoginMethod.APPLE  | null        | null             | null       | null         | "apple"  | Users.UserType.NORMAL  | null        | null           | null
        "APPLE: name이 null"            | LoginMethod.APPLE  | "appleUid"  | null             | null       | null         | null     | Users.UserType.NORMAL  | null        | null           | null
        "APPLE: userType이 null"        | LoginMethod.APPLE  | "appleUid"  | null             | null       | null         | "apple"  | null                   | null        | null           | null
        "APPLE: userType이 null"        | LoginMethod.APPLE  | "appleUid"  | null             | null       | null         | "apple"  | null                   | null        | null           | null
        "APPLE(보호소): 보호소 이름이 없는 경우"    | LoginMethod.APPLE  | "appleUid"  | null             | null       | null         | "apple"  | Users.UserType.SHELTER | null        | "보호소 주소"       | "01012345678"
        "APPLE(보호소): 보호소 주소이 없는 경우"    | LoginMethod.APPLE  | "appleUid"  | null             | null       | null         | "apple"  | Users.UserType.SHELTER | "보호소 이름"    | null           | "01012345678"
        "APPLE(보호소): 보호소 전화번호이 없는 경우"  | LoginMethod.APPLE  | "appleUid"  | null             | null       | null         | "apple"  | Users.UserType.SHELTER | "보호소 이름"    | "보호소 주소"       | null
        "EMAIL: loginMethod가 null"     | null               | "emailUid"  | "test@gmail.com" | "password" | null         | "email"  | Users.UserType.NORMAL  | null        | null           | null
        "EMAIL: uid가 null"             | LoginMethod.EMAIL  | null        | "test@gmail.com" | "password" | null         | "email"  | Users.UserType.NORMAL  | null        | null           | null
        "EMAIL: name이 null"            | LoginMethod.EMAIL  | "emailUid"  | "test@gmail.com" | "password" | null         | null     | Users.UserType.NORMAL  | null        | null           | null
        "EMAIL: email이 null"           | LoginMethod.EMAIL  | "emailUid"  | null             | "password" | null         | "email"  | Users.UserType.NORMAL  | null        | null           | null
        "EMAIL: password가 null"        | LoginMethod.EMAIL  | "emailUid"  | "test@gmail.com" | null       | null         | "email"  | Users.UserType.NORMAL  | null        | null           | null
        "EMAIL: userType이 null"        | LoginMethod.EMAIL  | "emailUid"  | "test@gmail.com" | "password" | null         | "email"  | null                   | null        | null           | null
        "EMAIL: userType이 null"        | LoginMethod.EMAIL  | "emailUid"  | "test@gmail.com" | "password" | null         | "email"  | null                   | null        | null           | null
        "EMAIL(보호소): 보호소 이름이 없는 경우"    | LoginMethod.EMAIL  | "emailUid"  | "test@gmail.com" | "password" | null         | "email"  | Users.UserType.SHELTER | null        | "보호소 주소"       | "01012345678"
        "EMAIL(보호소): 보호소 주소이 없는 경우"    | LoginMethod.EMAIL  | "emailUid"  | "test@gmail.com" | "password" | null         | "email"  | Users.UserType.SHELTER | "보호소 이름"    | null           | "01012345678"
        "EMAIL(보호소): 보호소 전화번호이 없는 경우"  | LoginMethod.EMAIL  | "emailUid"  | "test@gmail.com" | "password" | null         | "email"  | Users.UserType.SHELTER | "보호소 이름"    | "보호소 주소"       | null
    }

    def "로그인 성공: #testCase"() {
        setup:
        // 유저 추가
        userService.saveNewUser(
                Users.builder()
                        .loginMethod(loginMethod)
                        .uid(uid)
                        .email(email)
                        .password(password)
                        .profileImage(profileImage)
                        .name(name)
                        .userType(userType)
                        .build())

        def logInRequestDto = LogInRequestDto.builder()
                .loginMethod(loginMethod)
                .uid(uid)
                .email(email)
                .password(password)
                .build()

        expect:
        mvc.perform(post(USER_URL + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(logInRequestDto)))
                .andExpect(status().isOk())

        where:
        testCase    | loginMethod       | uid        | email            | password   | profileImage | name    | userType
        "SNS 로그인"   | LoginMethod.KAKAO | "kakaoUid" | null             | null       | null         | "kakao" | Users.UserType.NORMAL
        "EMAIL 로그인" | LoginMethod.EMAIL | "emailUid" | "test@gmail.com" | "password" | null         | "email" | Users.UserType.NORMAL
    }

    def "로그인 실패: #testCase"() {
        setup:
        def logInRequestDto = LogInRequestDto.builder()
                .loginMethod(loginMethod)
                .uid(uid)
                .email(email)
                .password(password)
                .build()

        expect:
        mvc.perform(post(USER_URL + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(logInRequestDto)))
                .andExpect(expectedStatus)

        where:
        testCase                | loginMethod       | uid        | email | password | expectedStatus
        "회원정보가 없는 경우"           | LoginMethod.KAKAO | "kakaoUid" | null  | null     | status().isNotFound()
        "SNS: 필수 데이터가 누락된 경우"   | null              | "kakaoUid" | null  | null     | status().isBadRequest()
        "EMAIL: 필수 데이터가 누락된 경우" | LoginMethod.EMAIL | "emailUid" | null  | null     | status().isBadRequest()
    }

}
