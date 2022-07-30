package com.leadpet.www.application.service

import com.leadpet.www.infrastructure.db.users.UsersRepository
import com.leadpet.www.infrastructure.db.users.condition.SearchShelterCondition
import com.leadpet.www.infrastructure.domain.users.AssessmentStatus
import com.leadpet.www.infrastructure.domain.users.LoginMethod
import com.leadpet.www.infrastructure.domain.users.UserType
import com.leadpet.www.infrastructure.domain.users.Users
import com.leadpet.www.infrastructure.exception.UnsatisfiedRequirementException
import com.leadpet.www.infrastructure.exception.login.UserNotFoundException
import com.leadpet.www.infrastructure.exception.signup.UserAlreadyExistsException
import com.leadpet.www.presentation.dto.response.user.ShelterPageResponseDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import spock.lang.Specification
import spock.lang.Unroll

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
        def dbResponse = createUser(userId, loginMethod, uid, email, password, profileImage, name, userType, shelterName, shelterAddress, shelterPhoneNumber, shelterManager, shelterHomePage)
        def newUser = createUser(null, loginMethod, uid, email, password, profileImage, name, userType, shelterName, shelterAddress, shelterPhoneNumber, shelterManager, shelterHomePage)
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
        savedUser.getUserType() == userType
        savedUser.getShelterName() == shelterName
        savedUser.getShelterAddress() == shelterAddress
        savedUser.getShelterPhoneNumber() == shelterPhoneNumber
        savedUser.getShelterManager() == shelterManager
        savedUser.getShelterHomePage() == shelterHomePage

        where:
        testCase     | loginMethod        | userId   | uid   | email             | password   | profileImage | name     | userType        | shelterName  | shelterAddress | shelterPhoneNumber | shelterManager | shelterHomePage
        "KAKAO"      | LoginMethod.KAKAO  | "uidkko" | "uid" | null              | null       | null         | "kakao"  | UserType.NORMAL | null         | null           | null               | null           | null
        "EMAIL"      | LoginMethod.EMAIL  | "uideml" | "uid" | "email@dummy.com" | "password" | null         | "email"  | UserType.NORMAL | null         | null           | null               | null           | null
        "GOOGLE"     | LoginMethod.GOOGLE | "uidggl" | "uid" | "email@gmail.com" | null       | null         | "google" | UserType.NORMAL | null         | null           | null               | null           | null
        "APPLE"      | LoginMethod.APPLE  | "uidapp" | "uid" | "email@apple.com" | null       | null         | "apple"  | UserType.NORMAL | null         | null           | null               | null           | null
        "KAKAO 보호소"  | LoginMethod.KAKAO  | "uidkko" | "uid" | null              | null       | null         | "kakao"  | UserType.NORMAL | "kakao 보호소"  | "kakao 1-2-3"  | "123-456-7890"     | "카톡"           | "www.kko.com"
        "EMAIL 보호소"  | LoginMethod.EMAIL  | "uideml" | "uid" | "email@dummy.com" | "password" | null         | "email"  | UserType.NORMAL | "email 보호소"  | "email 1-2-3"  | "123-456-7890"     | "이메일"          | "www.email.com"
        "GOOGLE 보호소" | LoginMethod.GOOGLE | "uidggl" | "uid" | "email@gmail.com" | null       | null         | "google" | UserType.NORMAL | "google 보호소" | "google 1-2-3" | "123-456-7890"     | "구글"           | "www.google.com"
        "APPLE 보호소"  | LoginMethod.APPLE  | "uidapp" | "uid" | "email@apple.com" | null       | null         | "apple"  | UserType.NORMAL | "apple 보호소"  | "apple 1-2-3"  | "123-456-7890"     | "애플"           | "www.apple.com"
    }

    def "이미 회원가입 상태라면 409-CONFLICT"() {
        setup:
        def existingUser = createUser('kakaoUidkko', LoginMethod.KAKAO, "kakaoUid", null, null, null, "kakao", UserType.NORMAL, null, null, null, null, null)
        usersRepository.findByLoginMethodAndUid(_, _) >> existingUser

        when:
        userService.saveNewUser(existingUser)

        then:
        thrown(UserAlreadyExistsException.class)
    }

    def "유저 로그인: 성공 케이스: #testCase"() {
        setup:
        def existingUser = createUser(userId, loginMethod, uid, email, password, null, name, userType, null, null, null, null, null)
        usersRepository.findByLoginMethodAndUid(_, _) >> existingUser

        when:
        def logInUser = userService.logIn(
                Users.builder()
                        .userType(userType)
                        .uid(uid)
                        .email(email)
                        .password(password)
                        .build())

        then:
        logInUser != null
        logInUser.getLoginMethod() == loginMethod
        logInUser.getUid() == uid
        logInUser.getName() == name
        logInUser.getUserType() == userType

        where:
        testCase    | loginMethod       | userId        | uid        | name    | userType        | email             | password
        "SNS 로그인"   | LoginMethod.KAKAO | "kakaoUidkko" | "kakaoUid" | "kakao" | UserType.NORMAL | null              | null
        "Email 로그인" | LoginMethod.EMAIL | "emailUideml" | "emailUid" | "email" | UserType.NORMAL | "email@email.com" | "password"
    }

    def "유저 로그인: 실패 케이스: #testCase"() {
        setup:
        usersRepository.findByLoginMethodAndUid(_, _) >> null

        when:
        userService.logIn(
                Users.builder()
                        .userType(userType)
                        .uid(uid)
                        .email(email)
                        .password(password)
                        .build())

        then:
        thrown(UserNotFoundException.class)

        where:
        testCase    | loginMethod       | uid        | name    | userType        | email             | password
        "SNS 로그인"   | LoginMethod.KAKAO | "kakaoUid" | "kakao" | UserType.NORMAL | null              | null
        "Email 로그인" | LoginMethod.EMAIL | "emailUid" | "email" | UserType.NORMAL | "email@email.com" | "password"
    }

    def "일반 유저 리스트를 받는다"() {
        given:
        usersRepository.findByUserType(_) >> [
                createUser('uid1kko', LoginMethod.KAKAO, 'uid1', null, null, null, "name1", UserType.NORMAL, null, null, null, null, null),
                createUser('uid2ggl', LoginMethod.GOOGLE, 'uid2', null, null, null, "name2", UserType.NORMAL, null, null, null, null, null),
                createUser('uid3eml', LoginMethod.EMAIL, 'uid3', "email@email.com", "password", null, "name3", UserType.NORMAL, null, null, null, null, null)
        ]

        when:
        List<Users> result = userService.getUserListBy(UserType.NORMAL)

        then:
        result.size() == 3
    }

    def "보호소의 피드 리스트 취득"() {
        given:
        usersRepository.searchShelters(_, _) >> new PageImpl<ShelterPageResponseDto>(
                List.of(
                        new ShelterPageResponseDto('userId1', "Shelter1", 3, AssessmentStatus.PENDING, 'profileImage'),
                        new ShelterPageResponseDto('userId2', "Shelter2", 2, AssessmentStatus.COMPLETED, 'profileImage'),
                        new ShelterPageResponseDto('userId3', "Shelter3", 1, AssessmentStatus.COMPLETED, 'profileImage')
                ),
                PageRequest.of(0, 5),
                3
        )

        when:
        Page<ShelterPageResponseDto> result = userService.searchShelters(new SearchShelterCondition(), PageRequest.of(0, 5))

        then:
        result.getContent().size() == 3
        result.getTotalPages() == 1
        result.getTotalElements() == 3
    }

    def "보호소 디테일 취득: 정상"() {
        given:
        usersRepository.findShelterByUserId(_) >>
                Users.builder()
                        .userId(userId)
                        .loginMethod(loginMethod)
                        .uid(uid)
                        .name(name)
                        .userType(userType)
                        .shelterName(shelterName)
                        .shelterAddress(shelterAddress)
                        .shelterAssessmentStatus(shelterAssessmentStatus)
                        .build()

        when:
        Users shelter = userService.shelterDetail(userId)

        then:
        shelter != null
        shelter.getUserId() == userId
        shelter.getLoginMethod() == loginMethod
        shelter.getUid() == uid
        shelter.getName() == name
        shelter.getUserType() == userType
        shelter.getShelterName() == shelterName
        shelter.getShelterAddress() == shelterAddress
        shelter.getShelterAssessmentStatus() == shelterAssessmentStatus

        where:
        userId   | loginMethod       | uid   | name   | userType         | shelterName | shelterAddress                 | shelterAssessmentStatus
        'userId' | LoginMethod.APPLE | 'uid' | 'name' | UserType.SHELTER | '토르 보호소'    | '서울특별시 헬로우 월드 주소 어디서나 123-123' | AssessmentStatus.PENDING
    }

    @Unroll("#testCase")
    def "보호소 디테일 취득: 에러"() {
        when:
        userService.shelterDetail(userId)

        then:
        thrown(exception)

        where:
        testCase             | userId     | exception
        'userId가 null인 경우'   | null       | UnsatisfiedRequirementException
        '존재하지 않는 userId인 경우' | 'notExist' | UserNotFoundException
    }

    // -------------------------------------------------------------------------------------

    private Users createUser(String userId, LoginMethod loginMethod, String uid, String email, String password, String profileImage,
                             String name, UserType userType, String shelterName, String shelterAddress, String shelterPhoneNumber,
                             String shelterManager, String shelterHomePage) {
        return Users.builder()
                .userId(userId)
                .loginMethod(loginMethod)
                .uid(uid)
                .email(email)
                .password(password)
                .profileImage(profileImage)
                .name(name)
                .userType(userType)
                .shelterName(shelterName)
                .shelterAddress(shelterAddress)
                .shelterPhoneNumber(shelterPhoneNumber)
                .shelterManager(shelterManager)
                .shelterHomePage(shelterHomePage)
                .build();
    }

}
