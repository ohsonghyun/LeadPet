package com.leadpet.www.application.service

import com.leadpet.www.infrastructure.db.UsersRepository
import com.leadpet.www.infrastructure.domain.users.LoginMethod
import com.leadpet.www.infrastructure.domain.users.Users
import com.leadpet.www.infrastructure.error.signup.UnsatisfiedRequirementException
import com.leadpet.www.infrastructure.error.signup.UserAlreadyExistsException
import spock.lang.Specification

/**
 * UserServiceSpec
 */
class UserServiceSpec extends Specification {

    private UserService userService
    private UsersRepository usersRepository

    def setup() {
        this.usersRepository = Mock(UsersRepository.class)
        this.userService = new UserService(usersRepository)
    }

    def "회원가입: #testCase"() {
        setup:
        def dbResponse = createUser(1, loginMethod, uid, email, password, profileImage, name, shelterName, shelterAddress, shelterPhoneNumber, shelterManager, shelterHomePage)
        def newUser = createUser(null, loginMethod, uid, email, password, profileImage, name, shelterName, shelterAddress, shelterPhoneNumber, shelterManager, shelterHomePage)
        usersRepository.save(_) >> dbResponse

        when:
        def savedUser = userService.saveNewUser(newUser)

        then:
        savedUser.getUserId() != null
        savedUser.getLoginMethod() == loginMethod
        savedUser.getUid() == uid
        savedUser.getEmail() == email
        savedUser.getPassword() == password
        savedUser.getProfileImage() == profileImage
        savedUser.getName() == name
        savedUser.getShelterName() == shelterName
        savedUser.getShelterAddress() == shelterAddress
        savedUser.getShelterPhoneNumber() == shelterPhoneNumber
        savedUser.getShelterManager() == shelterManager
        savedUser.getShelterHomePage() == shelterHomePage

        where:
        testCase     | loginMethod        | uid         | email             | password   | profileImage | name     | shelterName  | shelterAddress | shelterPhoneNumber | shelterManager | shelterHomePage
        "KAKAO"      | LoginMethod.KAKAO  | "kakaoUid"  | null              | null       | null         | "kakao"  | null         | null           | null               | null           | null
        "EMAIL"      | LoginMethod.EMAIL  | "email"     | "email@dummy.com" | "password" | null         | "email"  | null         | null           | null               | null           | null
        "GOOGLE"     | LoginMethod.GOOGLE | "googleUid" | "email@gmail.com" | null       | null         | "google" | null         | null           | null               | null           | null
        "APPLE"      | LoginMethod.APPLE  | "appleUid"  | "email@apple.com" | null       | null         | "apple"  | null         | null           | null               | null           | null
        "KAKAO 보호소"  | LoginMethod.KAKAO  | "kakaoUid"  | null              | null       | null         | "kakao"  | "kakao 보호소"  | "kakao 1-2-3"  | "123-456-7890"     | "카톡"           | "www.kko.com"
        "EMAIL 보호소"  | LoginMethod.EMAIL  | "email"     | "email@dummy.com" | "password" | null         | "email"  | "email 보호소"  | "email 1-2-3"  | "123-456-7890"     | "이메일"          | "www.email.com"
        "GOOGLE 보호소" | LoginMethod.GOOGLE | "googleUid" | "email@gmail.com" | null       | null         | "google" | "google 보호소" | "google 1-2-3" | "123-456-7890"     | "구글"           | "www.google.com"
        "APPLE 보호소"  | LoginMethod.APPLE  | "appleUid"  | "email@apple.com" | null       | null         | "apple"  | "apple 보호소"  | "apple 1-2-3"  | "123-456-7890"     | "애플"           | "www.apple.com"
    }

    def "회원가입: 필수입력 데이터가 없으면 에러: #testCase"() {
        setup:
        def newUser = createUser(null, loginMethod, uid, email, password, profileImage, name, null, null, null, null, null)

        when:
        userService.saveNewUser(newUser)

        then:
        thrown(UnsatisfiedRequirementException)

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

    def "이미 회원가입 상태라면 409-CONFLICT"() {
        setup:
        def existingUser = createUser(1, LoginMethod.KAKAO, "kakaoUid", null, null, null, "kakao", null, null, null, null, null)
        usersRepository.findByLoginMethodAndUid(_, _) >> existingUser

        when:
        userService.saveNewUser(existingUser)

        then:
        thrown(UserAlreadyExistsException)
    }

    private Users createUser(Long userId, LoginMethod loginMethod, String uid, String email, String password, String profileImage,
                             String name, String shelterName, String shelterAddress, String shelterPhoneNumber,
                             String shelterManager, String shelterHomePage) {
        return Users.builder()
                .userId(userId)
                .loginMethod(loginMethod)
                .uid(uid)
                .email(email)
                .password(password)
                .profileImage(profileImage)
                .name(name)
                .shelterName(shelterName)
                .shelterAddress(shelterAddress)
                .shelterPhoneNumber(shelterPhoneNumber)
                .shelterManager(shelterManager)
                .shelterHomePage(shelterHomePage)
                .build();
    }

}
