package com.leadpet.www.application.service

import com.leadpet.www.infrastructure.db.users.UsersRepository
import com.leadpet.www.infrastructure.db.users.condition.SearchShelterCondition
import com.leadpet.www.infrastructure.domain.users.AssessmentStatus
import com.leadpet.www.infrastructure.domain.users.LoginMethod
import com.leadpet.www.infrastructure.domain.users.ShelterInfo
import com.leadpet.www.infrastructure.domain.users.UserType
import com.leadpet.www.infrastructure.domain.users.Users
import com.leadpet.www.infrastructure.exception.UnsatisfiedRequirementException
import com.leadpet.www.infrastructure.exception.login.UserNotFoundException
import com.leadpet.www.infrastructure.exception.signup.UserAlreadyExistsException
import com.leadpet.www.presentation.dto.response.user.ShelterPageResponseDto
import com.leadpet.www.presentation.dto.response.user.UserDetailResponseDto
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
        def dbResponse = createUser(userId, loginMethod, uid, email, password, profileImage, name, userType, shelterName, shelterAddress, shelterPhoneNumber, shelterManager, shelterHomePage, shelterAccount, shelterIntro)
        def newUser = createUser(null, loginMethod, uid, email, password, profileImage, name, userType, shelterName, shelterAddress, shelterPhoneNumber, shelterManager, shelterHomePage, shelterAccount, shelterIntro)
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
        savedUser.getShelterIntro() == shelterIntro
        savedUser.getShelterAccount() == shelterAccount

        where:
        testCase     | loginMethod        | userId   | uid   | email             | password   | profileImage | name     | userType         | shelterName  | shelterAddress | shelterPhoneNumber | shelterManager | shelterHomePage  | shelterAccount   | shelterIntro
        "KAKAO"      | LoginMethod.KAKAO  | "uidkko" | "uid" | null              | null       | null         | "kakao"  | UserType.NORMAL  | null         | null           | null               | null           | null             | null             | null
        "EMAIL"      | LoginMethod.EMAIL  | "uideml" | "uid" | "email@dummy.com" | "password" | null         | "email"  | UserType.NORMAL  | null         | null           | null               | null           | null             | null             | null
        "GOOGLE"     | LoginMethod.GOOGLE | "uidggl" | "uid" | "email@gmail.com" | null       | null         | "google" | UserType.NORMAL  | null         | null           | null               | null           | null             | null             | null
        "APPLE"      | LoginMethod.APPLE  | "uidapp" | "uid" | "email@apple.com" | null       | null         | "apple"  | UserType.NORMAL  | null         | null           | null               | null           | null             | null             | null
        "KAKAO 보호소"  | LoginMethod.KAKAO  | "uidkko" | "uid" | null              | null       | null         | "kakao"  | UserType.SHELTER | "kakao 보호소"  | "kakao 1-2-3"  | "123-456-7890"     | "카톡"           | "www.kko.com"    | 'shelterAccount' | 'hello world'
        "EMAIL 보호소"  | LoginMethod.EMAIL  | "uideml" | "uid" | "email@dummy.com" | "password" | null         | "email"  | UserType.SHELTER | "email 보호소"  | "email 1-2-3"  | "123-456-7890"     | "이메일"          | "www.email.com"  | 'shelterAccount' | 'hello world'
        "GOOGLE 보호소" | LoginMethod.GOOGLE | "uidggl" | "uid" | "email@gmail.com" | null       | null         | "google" | UserType.SHELTER | "google 보호소" | "google 1-2-3" | "123-456-7890"     | "구글"           | "www.google.com" | 'shelterAccount' | 'hello world'
        "APPLE 보호소"  | LoginMethod.APPLE  | "uidapp" | "uid" | "email@apple.com" | null       | null         | "apple"  | UserType.SHELTER | "apple 보호소"  | "apple 1-2-3"  | "123-456-7890"     | "애플"           | "www.apple.com"  | 'shelterAccount' | 'hello world'
    }

    def "이미 회원가입 상태라면 409-CONFLICT"() {
        setup:
        def existingUser = createUser('kakaoUidkko', LoginMethod.KAKAO, "kakaoUid", null, null, null, "kakao", UserType.NORMAL, null, null, null, null, null, null, null)
        usersRepository.findByLoginMethodAndUid(_, _) >> existingUser

        when:
        userService.saveNewUser(existingUser)

        then:
        thrown(UserAlreadyExistsException.class)
    }

    def "유저 로그인: 성공 케이스: #testCase"() {
        setup:
        def existingUser = createUser(userId, loginMethod, uid, email, password, null, name, userType, null, null, null, null, null, null, null)
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
                createUser('uid1kko', LoginMethod.KAKAO, 'uid1', null, null, null, "name1", UserType.NORMAL, null, null, null, null, null, null, null),
                createUser('uid2ggl', LoginMethod.GOOGLE, 'uid2', null, null, null, "name2", UserType.NORMAL, null, null, null, null, null, null, null),
                createUser('uid3eml', LoginMethod.EMAIL, 'uid3', "email@email.com", "password", null, "name3", UserType.NORMAL, null, null, null, null, null, null, null)
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
                        new ShelterPageResponseDto('userId1', "Shelter1", 3, AssessmentStatus.PENDING, "헬로우 월드 123-123", "010-1234-1233", "www.shelter1.com", 'profileImage'),
                        new ShelterPageResponseDto('userId2', "Shelter2", 2, AssessmentStatus.COMPLETED, "헬로우 월드 123-122", "010-1234-1232", "www.shelter2.com", 'profileImage'),
                        new ShelterPageResponseDto('userId3', "Shelter3", 1, AssessmentStatus.COMPLETED, "헬로우 월드 123-121", "010-1234-1231", "www.shelter3.com", 'profileImage')
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
        result.getContent().get(0).profileImage == 'profileImage'
        result.getContent().get(0).getUserId() == 'userId1'
        result.getContent().get(0).getShelterName() == 'Shelter1'
        result.getContent().get(0).getShelterAddress() == '헬로우 월드 123-123'
        result.getContent().get(0).getShelterPhoneNumber() == '010-1234-1233'
        result.getContent().get(0).getShelterHomePage() == 'www.shelter1.com'
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
                        .shelterIntro(shelterIntro)
                        .shelterAccount(shelterAccount)
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
        shelter.getShelterIntro() == shelterIntro
        shelter.getShelterAccount() == shelterAccount

        where:
        userId   | loginMethod       | uid   | name   | userType         | shelterName | shelterAddress                 | shelterIntro   | shelterAccount   | shelterAssessmentStatus
        'userId' | LoginMethod.APPLE | 'uid' | 'name' | UserType.SHELTER | '토르 보호소'    | '서울특별시 헬로우 월드 주소 어디서나 123-123' | 'shelterIntro' | 'shelterAccount' | AssessmentStatus.PENDING
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


    // TODO 역시 보호소 디테일하고 내부 로직은 공유하고 서비스에서 따로 분리하는게 좋을까?
    // DRY 관점으로 정말 동일로직으로 합쳐도 될지 생각 필요. 판단 후에 리팩토링
    @Unroll("#testCase")
    def "일반 유저 디테일 취득: 에러"() {
        when:
        userService.normalUserDetail(userId)

        then:
        thrown(exception)

        where:
        testCase             | userId     | exception
        'userId가 null인 경우'   | null       | UnsatisfiedRequirementException
        '존재하지 않는 userId인 경우' | 'notExist' | UserNotFoundException
    }

    @Unroll
    def "[보호소 정보 수정] 정상"() {
        given:
        usersRepository.findShelterByUserId(userId) >> Users.builder()
                .loginMethod(LoginMethod.KAKAO)
                .uid('uid')
                .name('name')
                .userId(userId)
                .userType(UserType.SHELTER)
                .shelterName('newShelterName')
                .shelterAddress('shelterAddress')
                .shelterManager('shelterManager')
                .shelterHomePage('shelterHomePage')
                .shelterPhoneNumber('01012341234')
                .shelterIntro('shelterIntro')
                .shelterAccount('shelterAccount')
                .build()

        def newShelterInfo = ShelterInfo.builder()
                .shelterName(newShelterName)
                .shelterAddress(newShelterAddress)
                .shelterPhoneNumber(newShelterPhoneNumber)
                .shelterIntro(newShelterIntro)
                .shelterAccount(newShelterAccount)
                .shelterManager(newShelterManager)
                .shelterHomePage(newShelterHomePage)
                .build()

        when:
        Users updatedShelter = userService.updateShetlerInfo(userId, newShelterInfo)

        then:
        updatedShelter.getShelterName() == newShelterName
        updatedShelter.getShelterAddress() == newShelterAddress
        updatedShelter.getShelterPhoneNumber() == newShelterPhoneNumber
        updatedShelter.getShelterIntro() == newShelterIntro
        updatedShelter.getShelterAccount() == newShelterAccount
        updatedShelter.getShelterManager() == newShelterManager
        updatedShelter.getShelterHomePage() == newShelterHomePage

        where:
        userId   | newShelterName   | newShelterAddress   | newShelterPhoneNumber | newShelterIntro   | newShelterAccount   | newShelterManager   | newShelterHomePage
        'userId' | 'newShelterName' | 'newShelterAddress' | '01056785678'         | 'newShelterIntro' | 'newShelterAccount' | 'newShelterManager' | 'newShelterHomePage'
    }

    @Unroll
    def "[보호소 정보 수정] 존재하지 않는 유저 에러"() {
        given:
        def newShelterInfo = ShelterInfo.builder().build()

        when:
        userService.updateShetlerInfo('userId', newShelterInfo)

        then:
        thrown(UserNotFoundException)
    }

    def "일반 유저 디테일 취득: 정상"() {
        given:
        usersRepository.findNormalUserDetailByUserId(_) >>
                UserDetailResponseDto.builder()
                        .userId(userId)
                        .email(email)
                        .build()

        when:
        UserDetailResponseDto userDetailResponseDto = userService.normalUserDetail(userId)

        then:
        userDetailResponseDto != null
        userDetailResponseDto.getUserId() == userId
        userDetailResponseDto.getEmail() == email

        where:
        userId   | email
        'userId' | 'test@email.com'
    }

    // -------------------------------------------------------------------------------------

    private Users createUser(String userId, LoginMethod loginMethod, String uid, String email, String password, String profileImage,
                             String name, UserType userType, String shelterName, String shelterAddress, String shelterPhoneNumber,
                             String shelterManager, String shelterHomePage, String shelterAccount, String shelterIntro) {
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
                .shelterAccount(shelterAccount)
                .shelterIntro(shelterIntro)
                .build();
    }

}
