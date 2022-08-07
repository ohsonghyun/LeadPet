package com.leadpet.www.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.leadpet.www.application.service.UserService
import com.leadpet.www.infrastructure.db.users.UsersRepository
import com.leadpet.www.infrastructure.domain.users.LoginMethod
import com.leadpet.www.infrastructure.domain.users.UserType
import com.leadpet.www.infrastructure.domain.users.Users
import com.leadpet.www.infrastructure.exception.login.UserNotFoundException
import com.leadpet.www.presentation.dto.request.user.LogInRequestDto
import com.leadpet.www.presentation.dto.request.user.SignUpUserRequestDto
import org.hamcrest.Matchers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification
import spock.lang.Unroll

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * UserControllerSpec
 */
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

    // TODO 일반 유저 가입 성공 테스트 케이스가 없다... 왜지...
    @Unroll
    def "회원가입 성공"() {
        setup:
        def signUpUserRequestDto = SignUpUserRequestDto.builder()
                .loginMethod(LoginMethod.KAKAO)
                .uid('uid')
                .name('name')
                .userType(UserType.SHELTER)
                .shelterName('shelterName')
                .shelterPhoneNumber('01012341234')
                .shelterAddress('hello world address')
                .shelterIntro('We are the best shelter')
                .shelterAccount('Dev6 Bank 000-000-000')
                .build()

        expect:
        mvc.perform(post(USER_URL + "/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(signUpUserRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath('\$.uid').isNotEmpty())
    }

    @Unroll
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
                .shelterIntro(shelterIntro)
                .build()

        expect:
        mvc.perform(post(USER_URL + "/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(signUpUserRequestDto)))
                .andExpect(status().isBadRequest());

        where:
        testCase                       | loginMethod        | uid         | email            | password   | profileImage | name     | userType         | shelterName | shelterAddress | shelterPhoneNumber | shelterIntro
        "KAKAO: loginMethod가 null"     | null               | "kakaoUid"  | null             | null       | null         | "kakao"  | UserType.NORMAL  | null        | null           | null               | null
        "KAKAO: uid가 null"             | LoginMethod.KAKAO  | null        | null             | null       | null         | "kakao"  | UserType.NORMAL  | null        | null           | null               | null
        "KAKAO: name이 null"            | LoginMethod.KAKAO  | "kakaoUid"  | null             | null       | null         | null     | UserType.NORMAL  | null        | null           | null               | null
        "KAKAO: userType이 null"        | LoginMethod.KAKAO  | "kakaoUid"  | null             | null       | null         | "kakao"  | null             | null        | null           | null               | null
        "KAKAO(보호소): 보호소 이름이 없는 경우"    | LoginMethod.KAKAO  | "kakaoUid"  | null             | null       | null         | "kakao"  | UserType.SHELTER | null        | "보호소 주소"       | "01012345678"      | null
        "KAKAO(보호소): 보호소 주소이 없는 경우"    | LoginMethod.KAKAO  | "kakaoUid"  | null             | null       | null         | "kakao"  | UserType.SHELTER | "보호소 이름"    | null           | "01012345678"      | null
        "KAKAO(보호소): 보호소 전화번호이 없는 경우"  | LoginMethod.KAKAO  | "kakaoUid"  | null             | null       | null         | "kakao"  | UserType.SHELTER | "보호소 이름"    | "보호소 주소"       | null               | null
        "GOOGLE: loginMethod가 null"    | null               | "googleUid" | null             | null       | null         | "google" | UserType.NORMAL  | null        | null           | null               | null
        "GOOGLE: uid가 null"            | LoginMethod.GOOGLE | null        | null             | null       | null         | "google" | UserType.NORMAL  | null        | null           | null               | null
        "GOOGLE: name이 null"           | LoginMethod.GOOGLE | "googleUid" | null             | null       | null         | null     | UserType.NORMAL  | null        | null           | null               | null
        "GOOGLE: userType이 null"       | LoginMethod.GOOGLE | "googleUid" | null             | null       | null         | "google" | null             | null        | null           | null               | null
        "GOOGLE(보호소): 보호소 이름이 없는 경우"   | LoginMethod.GOOGLE | "googleUid" | null             | null       | null         | "google" | UserType.SHELTER | null        | "보호소 주소"       | "01012345678"      | null
        "GOOGLE(보호소): 보호소 주소이 없는 경우"   | LoginMethod.GOOGLE | "googleUid" | null             | null       | null         | "google" | UserType.SHELTER | "보호소 이름"    | null           | "01012345678"      | null
        "GOOGLE(보호소): 보호소 전화번호이 없는 경우" | LoginMethod.GOOGLE | "googleUid" | null             | null       | null         | "google" | UserType.SHELTER | "보호소 이름"    | "보호소 주소"       | null               | null
        "APPLE: loginMethod가 null"     | null               | "appleUid"  | null             | null       | null         | "apple"  | UserType.NORMAL  | null        | null           | null               | null
        "APPLE: uid가 null"             | LoginMethod.APPLE  | null        | null             | null       | null         | "apple"  | UserType.NORMAL  | null        | null           | null               | null
        "APPLE: name이 null"            | LoginMethod.APPLE  | "appleUid"  | null             | null       | null         | null     | UserType.NORMAL  | null        | null           | null               | null
        "APPLE: userType이 null"        | LoginMethod.APPLE  | "appleUid"  | null             | null       | null         | "apple"  | null             | null        | null           | null               | null
        "APPLE: userType이 null"        | LoginMethod.APPLE  | "appleUid"  | null             | null       | null         | "apple"  | null             | null        | null           | null               | null
        "APPLE(보호소): 보호소 이름이 없는 경우"    | LoginMethod.APPLE  | "appleUid"  | null             | null       | null         | "apple"  | UserType.SHELTER | null        | "보호소 주소"       | "01012345678"      | null
        "APPLE(보호소): 보호소 주소이 없는 경우"    | LoginMethod.APPLE  | "appleUid"  | null             | null       | null         | "apple"  | UserType.SHELTER | "보호소 이름"    | null           | "01012345678"      | null
        "APPLE(보호소): 보호소 전화번호이 없는 경우"  | LoginMethod.APPLE  | "appleUid"  | null             | null       | null         | "apple"  | UserType.SHELTER | "보호소 이름"    | "보호소 주소"       | null               | null
        "EMAIL: loginMethod가 null"     | null               | "emailUid"  | "test@gmail.com" | "password" | null         | "email"  | UserType.NORMAL  | null        | null           | null               | null
        "EMAIL: uid가 null"             | LoginMethod.EMAIL  | null        | "test@gmail.com" | "password" | null         | "email"  | UserType.NORMAL  | null        | null           | null               | null
        "EMAIL: name이 null"            | LoginMethod.EMAIL  | "emailUid"  | "test@gmail.com" | "password" | null         | null     | UserType.NORMAL  | null        | null           | null               | null
        "EMAIL: email이 null"           | LoginMethod.EMAIL  | "emailUid"  | null             | "password" | null         | "email"  | UserType.NORMAL  | null        | null           | null               | null
        "EMAIL: password가 null"        | LoginMethod.EMAIL  | "emailUid"  | "test@gmail.com" | null       | null         | "email"  | UserType.NORMAL  | null        | null           | null               | null
        "EMAIL: userType이 null"        | LoginMethod.EMAIL  | "emailUid"  | "test@gmail.com" | "password" | null         | "email"  | null             | null        | null           | null               | null
        "EMAIL: userType이 null"        | LoginMethod.EMAIL  | "emailUid"  | "test@gmail.com" | "password" | null         | "email"  | null             | null        | null           | null               | null
        "EMAIL(보호소): 보호소 이름이 없는 경우"    | LoginMethod.EMAIL  | "emailUid"  | "test@gmail.com" | "password" | null         | "email"  | UserType.SHELTER | null        | "보호소 주소"       | "01012345678"      | null
        "EMAIL(보호소): 보호소 주소이 없는 경우"    | LoginMethod.EMAIL  | "emailUid"  | "test@gmail.com" | "password" | null         | "email"  | UserType.SHELTER | "보호소 이름"    | null           | "01012345678"      | null
        "EMAIL(보호소): 보호소 전화번호이 없는 경우"  | LoginMethod.EMAIL  | "emailUid"  | "test@gmail.com" | "password" | null         | "email"  | UserType.SHELTER | "보호소 이름"    | "보호소 주소"       | null               | null
    }

    @Unroll
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
                .andExpect(jsonPath('$.userId').value(userId))

        where:
        testCase    | loginMethod       | uid   | email            | password   | profileImage | name    | userType        | userId
        "SNS 로그인"   | LoginMethod.KAKAO | "uid" | null             | null       | null         | "kakao" | UserType.NORMAL | 'uidkko'
        "EMAIL 로그인" | LoginMethod.EMAIL | "uid" | "test@gmail.com" | "password" | null         | "email" | UserType.NORMAL | 'uideml'
    }

    @Unroll
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

    @Unroll('#testCase')
    def "유저 타입별 리스트 획득"() {
        given:
        userService.saveNewUser(Users.builder().loginMethod(LoginMethod.KAKAO).uid('uid1').name('name1').userType(UserType.NORMAL).build())
        userService.saveNewUser(Users.builder().loginMethod(LoginMethod.GOOGLE).uid('uid2').name('name2').userType(UserType.NORMAL).build())
        userService.saveNewUser(Users.builder().loginMethod(LoginMethod.GOOGLE).uid('uid3').name('name3').userType(UserType.SHELTER).shelterName("shelter").shelterAddress("address").shelterPhoneNumber('01012341234').build())
        userService.saveNewUser(Users.builder().loginMethod(LoginMethod.EMAIL).uid('uid4').email("email@email.com").password("password").name('name4').userType(UserType.NORMAL).build())

        expect:
        mvc.perform(get(USER_URL + '/list').param('ut', paramValue))
                .andExpect(status().isOk())
                .andExpect(jsonPath("\$", Matchers.hasSize(expectedSize)))

        where:
        testCase | paramValue | userType         | expectedSize
        '일반유저'   | 'normal'   | UserType.NORMAL  | 3
        '보호소'    | 'shelter'  | UserType.SHELTER | 1
    }

    def "유저 타입별 리스트 획득: 에러 케이스: WrongArgumentsException"() {
        expect:
        mvc.perform(get(USER_URL + '/list').param('ut', 'wrongParam'))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath('\$.error.detail').value('Error: 잘못 된 파라미터'))
    }

    @Unroll
    def "[유저 디테일 취득]정상"() {
        given:
        userService.saveNewUser(Users.builder().loginMethod(LoginMethod.KAKAO).uid('uid1').email(email).name('name1').userType(UserType.NORMAL).build())

        expect:
        mvc.perform(get(USER_URL + '/' + userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath('\$.userId').value(userId))
                .andExpect(jsonPath('\$.email').value(email))
        // TODO allReplyCount 도 확인해야하는데.. 전체적으로 테스트 리팩토링할 때 같이 넣기..

        where:
        userId    | email            | shelterPhoneNumber
        'uid1kko' | 'test@gmail.com' | ''
    }

    def "[유저 디테일 취득]에러: #testCase"() {
        expect:
        mvc.perform(get(USER_URL + '/' + userId))
                .andExpect(responseStatus)

        where:
        testCase             | expectedException                              | userId     | responseStatus
        '존재하지 않는 userId인 경우' | new UserNotFoundException("Error: 존재하지 않는 유저") | 'notExist' | status().isNotFound()
    }
}
