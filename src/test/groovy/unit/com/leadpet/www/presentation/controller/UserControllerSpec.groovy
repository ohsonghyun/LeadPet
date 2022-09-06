package com.leadpet.www.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.leadpet.www.application.service.UserService
import com.leadpet.www.infrastructure.db.users.condition.SearchUserCondition
import com.leadpet.www.infrastructure.domain.users.AssessmentStatus
import com.leadpet.www.infrastructure.domain.users.LoginMethod
import com.leadpet.www.infrastructure.domain.users.ShelterInfo
import com.leadpet.www.infrastructure.domain.users.UserInfo
import com.leadpet.www.infrastructure.domain.users.UserType
import com.leadpet.www.infrastructure.domain.users.Users
import com.leadpet.www.infrastructure.exception.UnauthorizedUserException
import com.leadpet.www.infrastructure.exception.UnsatisfiedRequirementException
import com.leadpet.www.infrastructure.exception.login.UserNotFoundException
import com.leadpet.www.presentation.dto.request.shelter.UpdateShelterInfoRequestDto
import com.leadpet.www.presentation.dto.request.user.LogInRequestDto
import com.leadpet.www.presentation.dto.request.user.SignUpUserRequestDto
import com.leadpet.www.presentation.dto.request.user.UpdateUserInfoRequestDto
import com.leadpet.www.presentation.dto.response.user.UserDetailResponseDto
import com.leadpet.www.presentation.dto.response.user.UserListResponseDto
import org.hamcrest.Matchers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import spock.lang.Unroll

import static org.mockito.ArgumentMatchers.eq
import static org.mockito.ArgumentMatchers.isA
import static org.mockito.Mockito.when
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(UserController.class)
class UserControllerSpec extends Specification {
    private final String USER_URL = '/v1/user'

    @Autowired
    private MockMvc mvc

    @MockBean
    private UserService userService

    // TODO 일반 유저 가입 성공 테스트 케이스가 없다... 왜지...
    @Unroll
    def "[ #testCase ] 회원가입 성공"() {
        given:
        when(userService.saveNewUser(isA(Users.class)))
                .thenReturn(
                        Users.builder()
                                .uid(uid)
                                .name(userName)
                                .loginMethod(loginMethod)
                                .userType(userType)
                                .shelterName(shelterName)
                                .shelterPhoneNumber(shelterPhoneNumber)
                                .shelterAddress(shelterAddress)
                                .shelterIntro(shelterIntro)
                                .shelterAccount(shelterAccount)
                                .build()
                )

        expect:
        mvc.perform(post(USER_URL + "/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(
                        SignUpUserRequestDto.builder()
                                .loginMethod(loginMethod)
                                .uid(uid)
                                .name(userName)
                                .userType(userType)
                                .shelterName(shelterName)
                                .shelterPhoneNumber(shelterPhoneNumber)
                                .shelterAddress(shelterAddress)
                                .shelterIntro(shelterIntro)
                                .shelterAccount(shelterAccount)
                                .build()
                )))
                .andExpect(status().isOk())
                .andExpect(jsonPath('\$.uid').value(uid))

        where:
        testCase | uid   | userName   | loginMethod       | userType         | shelterName   | shelterPhoneNumber | shelterAddress   | shelterIntro   | shelterAccount
        '보호소'    | 'uid' | 'userName' | LoginMethod.KAKAO | UserType.SHELTER | 'shelterName' | '01012341234'      | 'shelterAddress' | 'shelterIntro' | 'shelterAccount'
        '일반유저'   | 'uid' | 'userName' | LoginMethod.KAKAO | UserType.NORMAL  | null          | null               | null             | null           | null

    }

    @Unroll
    def "회원가입 validation: #testCase"() throws Exception {
        setup:
        when(userService.saveNewUser(isA(Users.class)))
                .thenThrow(new UnsatisfiedRequirementException("Error: 필수 입력 데이터 누락"))

        expect:
        mvc.perform(post(USER_URL + "/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(
                        SignUpUserRequestDto.builder()
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
                )))
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
        given:
        when(userService.logIn(isA(Users.class)))
                .thenReturn(
                        Users.builder()
                                .loginMethod(loginMethod)
                                .uid(uid)
                                .userId(userId)
                                .email(email)
                                .password(password)
                                .profileImage(profileImage)
                                .name(name)
                                .userType(userType)
                                .build()
                )
        expect:
        mvc.perform(post(USER_URL + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(
                        LogInRequestDto.builder()
                                .loginMethod(loginMethod)
                                .uid(uid)
                                .email(email)
                                .password(password)
                                .userType(userType)
                                .build()
                )))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.userId').value(userId))
                .andExpect(jsonPath('$.uid').value(uid))
                .andExpect(jsonPath('$.profileImage').value(profileImage))
                .andExpect(jsonPath('$.userType').value(userType.toString()))

        where:
        testCase    | loginMethod       | uid   | email            | password   | profileImage | name    | userType        | userId
        "SNS 로그인"   | LoginMethod.KAKAO | "uid" | null             | null       | null         | "kakao" | UserType.NORMAL | 'uidkko'
        "EMAIL 로그인" | LoginMethod.EMAIL | "uid" | "test@gmail.com" | "password" | null         | "email" | UserType.NORMAL | 'uideml'
    }

    @Unroll
    def "로그인 실패: #testCase"() {
        given:
        when(userService.logIn(isA(Users.class))).thenThrow(exception)

        expect:
        mvc.perform(post(USER_URL + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(
                        LogInRequestDto.builder()
                                .loginMethod(loginMethod)
                                .uid(uid)
                                .email(email)
                                .password(password)
                                .userType(userType)
                                .build()
                )))
                .andExpect(expectedStatus)

        where:
        testCase                | loginMethod       | uid        | email | password | userType        | exception                               | expectedStatus
        "회원정보가 없는 경우"           | LoginMethod.KAKAO | "kakaoUid" | null  | null     | UserType.NORMAL | new UserNotFoundException()             | status().isNotFound()
        "SNS: 필수 데이터가 누락된 경우"   | null              | "kakaoUid" | null  | null     | UserType.NORMAL | new UnsatisfiedRequirementException("") | status().isBadRequest()
        "EMAIL: 필수 데이터가 누락된 경우" | LoginMethod.EMAIL | "emailUid" | null  | null     | UserType.NORMAL | new UnsatisfiedRequirementException("") | status().isBadRequest()
        "관리자가 로그인한 경우"          | LoginMethod.EMAIL | "emailUid" | null  | null     | UserType.ADMIN  | new UnauthorizedUserException("")       | status().isForbidden()
    }

    @Unroll
    def "관리자 로그인 성공: #testCase"() {
        given:
        when(userService.logIn(isA(Users.class)))
                .thenReturn(
                        Users.builder()
                                .loginMethod(loginMethod)
                                .uid(uid)
                                .userId(userId)
                                .email(email)
                                .password(password)
                                .profileImage(profileImage)
                                .name(name)
                                .userType(userType)
                                .build()
                )
        expect:
        mvc.perform(post(USER_URL + "/adminLogin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(
                        LogInRequestDto.builder()
                                .loginMethod(loginMethod)
                                .uid(uid)
                                .email(email)
                                .password(password)
                                .userType(userType)
                                .build()
                )))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.userId').value(userId))

        where:
        testCase    | loginMethod       | uid   | email            | password   | profileImage | name    | userType       | userId
        "EMAIL 로그인" | LoginMethod.EMAIL | "uid" | "test@gmail.com" | "password" | null         | "email" | UserType.ADMIN | 'uideml'
    }

    @Unroll
    def "관리자 로그인 실패: #testCase"() {
        given:
        when(userService.logIn(isA(Users.class))).thenThrow(exception)

        expect:
        mvc.perform(post(USER_URL + "/adminLogin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(
                        LogInRequestDto.builder()
                                .loginMethod(loginMethod)
                                .uid(uid)
                                .email(email)
                                .password(password)
                                .userType(userType)
                                .build()
                )))
                .andExpect(expectedStatus)

        where:
        testCase                | loginMethod       | uid        | email            | password   | userType        | exception                               | expectedStatus
        "회원정보가 없는 경우"           | LoginMethod.EMAIL | "emailUid" | "test@gmail.com" | "password" | UserType.ADMIN  | new UserNotFoundException()             | status().isNotFound()
        "EMAIL: 필수 데이터가 누락된 경우" | LoginMethod.EMAIL | "emailUid" | null             | null       | UserType.ADMIN  | new UnsatisfiedRequirementException("") | status().isBadRequest()
        "관리자가 아닌 경우"            | LoginMethod.EMAIL | "emailUid" | null             | null       | UserType.NORMAL | new UnauthorizedUserException("")       | status().isForbidden()
    }

    @Unroll('#testCase')
    def "유저 타입별 리스트 획득"() {
        given:
        when(userService.searchUsers(isA(SearchUserCondition.class), isA(Pageable.class)))
                .thenReturn(new PageImpl<UserListResponseDto>(
                        List.of(new UserListResponseDto(loginMethod, uid, email, profileImage, name, userType, shelterName, shelterAddress, shelterPhoneNumber, shelterManager, shelterHomePage))
                ))

        expect:
        mvc.perform(get(USER_URL + '/list')
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath('\$.content[0].loginMethod').value(loginMethod.toString()))
                .andExpect(jsonPath('\$.content[0].uid').value(uid))
                .andExpect(jsonPath('\$.content[0].email').value(email))
                .andExpect(jsonPath('\$.content[0].profileImage').value(profileImage))
                .andExpect(jsonPath('\$.content[0].name').value(name))
                .andExpect(jsonPath('\$.content[0].userType').value(userType.toString()))
                .andExpect(jsonPath('\$.content[0].shelterName').value(shelterName))
                .andExpect(jsonPath('\$.content[0].shelterAddress').value(shelterAddress))
                .andExpect(jsonPath('\$.content[0].shelterPhoneNumber').value(shelterPhoneNumber))
                .andExpect(jsonPath('\$.content[0].shelterManager').value(shelterManager))
                .andExpect(jsonPath('\$.content[0].shelterHomePage').value(shelterHomePage))

        where:
        testCase | loginMethod         | uid     | email  | profileImage | name   | userType            | shelterName | shelterAddress      | shelterPhoneNumber | shelterManager | shelterHomePage
        '일반유저' | LoginMethod.KAKAO   | 'uid1' | null   | 'profile1'    | 'name' | UserType.NORMAL    | null         | null                | null               | null           |  null
        '보호소'  | LoginMethod.KAKAO    | 'uid2' | null   | 'profile2'    | null   | UserType.SHELTER   | 'shelter'    | '헬로우 월드 123-123' | '010-1234-1234'    | 'manager'      |  'www.shelter.com'
    }

    @Unroll
    def "[유저 디테일 취득]정상"() {
        given:
        when(userService.normalUserDetail(isA(String.class)))
                .thenReturn(
                        UserDetailResponseDto.builder()
                                .userId(userId)
                                .userName(userName)
                                .email(email)
                                .intro(intro)
                                .address(address)
                                .profileImage(profileImage)
                                .allReplyCount(allReplyCount)
                                .build())

        expect:
        mvc.perform(get(USER_URL + '/' + userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath('\$.userId').value(userId))
                .andExpect(jsonPath('\$.userName').value(userName))
                .andExpect(jsonPath('\$.email').value(email))
                .andExpect(jsonPath('\$.allReplyCount').value(allReplyCount))

        where:
        userId    | userName   | email            | shelterPhoneNumber | intro   | address   | profileImage   | allReplyCount
        'uid1kko' | 'userName' | 'test@gmail.com' | ''                 | 'intro' | 'address' | 'profileImage' | 2
    }

    def "[유저 디테일 취득]에러: #testCase"() {
        given:
        when(userService.normalUserDetail(isA(String.class)))
                .thenThrow(expectedException)

        expect:
        mvc.perform(get(USER_URL + '/' + userId))
                .andExpect(responseStatus)

        where:
        testCase             | expectedException                              | userId     | responseStatus
        '존재하지 않는 userId인 경우' | new UserNotFoundException("Error: 존재하지 않는 유저") | 'notExist' | status().isNotFound()
    }

    def "[일반유저 정보 수정] 정상"() {
        given:
        when(userService.updateNormalUser(isA(String.class), isA(UserInfo.class)))
                .thenReturn(
                        Users.builder()
                                .loginMethod(LoginMethod.KAKAO)
                                .uid('uid')
                                .name(name)
                                .userId(userId)
                                .name(name)
                                .intro(intro)
                                .address(address)
                                .profileImage(profileImage)
                                .userType(UserType.NORMAL)
                                .build()
                )

        expect:
        mvc.perform(put(USER_URL + '/' + userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(
                        UpdateUserInfoRequestDto.builder()
                                .name(name)
                                .intro(intro)
                                .address(address)
                                .profileImage(profileImage)
                                .build()
                )))
                .andExpect(status().isOk())
                .andExpect(jsonPath('\$.userId').value(userId))
                .andExpect(jsonPath('\$.name').value(name))
                .andExpect(jsonPath('\$.intro').value(intro))
                .andExpect(jsonPath('\$.address').value(address))
                .andExpect(jsonPath('\$.profileImage').value(profileImage))

        where:
        userId   | name   | intro   | profileImage   | address
        'userId' | 'name' | 'intro' | 'profileImage' | 'address'
    }

}
