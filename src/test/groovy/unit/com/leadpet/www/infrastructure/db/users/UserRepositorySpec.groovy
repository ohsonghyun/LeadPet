package com.leadpet.www.infrastructure.db.users

import com.leadpet.www.TestConfig
import com.leadpet.www.infrastructure.db.posts.normalPost.NormalPostsRepository
import com.leadpet.www.infrastructure.db.reply.normal.NormalReplyRepository
import com.leadpet.www.infrastructure.db.users.condition.SearchShelterCondition
import com.leadpet.www.infrastructure.domain.posts.AdoptionPosts
import com.leadpet.www.infrastructure.domain.posts.DonationPosts
import com.leadpet.www.infrastructure.domain.posts.NormalPosts
import com.leadpet.www.infrastructure.domain.reply.normal.NormalReply
import com.leadpet.www.infrastructure.domain.users.AssessmentStatus
import com.leadpet.www.infrastructure.domain.users.LoginMethod
import com.leadpet.www.infrastructure.domain.users.UserType
import com.leadpet.www.infrastructure.domain.users.Users
import com.leadpet.www.presentation.dto.response.user.ShelterPageResponseDto
import com.leadpet.www.presentation.dto.response.user.UserDetailResponseDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageRequest
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification
import spock.lang.Unroll

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import java.util.stream.IntStream
import java.util.stream.LongStream

/**
 * UserRepositorySpec
 */
@DataJpaTest
@Import(TestConfig.class)
@Transactional
class UserRepositorySpec extends Specification {

    @PersistenceContext
    EntityManager em

    @Autowired
    UsersRepository usersRepository
    @Autowired
    NormalPostsRepository normalPostsRepository
    @Autowired
    NormalReplyRepository normalReplyRepository

    @Unroll(value = "#testName")
    def "보호소 리스트 취득"() {
        given:
        // 보호소 추가
        IntStream.range(0, 20).forEach(idx -> {
            final String name = 'Hulk' + idx;
            final String city = idx % 2 == 0 ? '서울특별시' : '부산광역시'
            usersRepository.save(
                    Users.builder()
                            .userId("app" + idx)
                            .loginMethod(LoginMethod.APPLE)
                            .uid("uid" + idx)
                            .name(name)
                            .userType(UserType.SHELTER)
                            .shelterName(name + " 보호소")
                            .shelterAddress(city + " 헬로우 월드 주소 어디서나 123-123")
                            .shelterAssessmentStatus(assessmentStatus)
                            .shelterHomePage("www." + name + ".com")
                            .shelterPhoneNumber("010-" + idx + "12-1234")
                            .profileImage(profileImage)
                            .build())

        })

        // 종류별 Feed 추가
        def allUsers = usersRepository.findAll()
        for (Users user : allUsers) {
            for (int i = 0; i < 3; i++) {
                em.persist(
                        NormalPosts.builder()
                                .normalPostId('NP_' + user.getUserId() + i)
                                .title('title' + user.getUserId() + i)
                                .contents('contents' + user.getUserId() + i)
                                .user(user)
                                .build())
                em.persist(
                        DonationPosts.builder()
                                .donationPostId('DP_' + user.getUserId() + i)
                                .title('title' + user.getUserId() + i)
                                .contents('contents' + user.getUserId() + i)
                                .user(user)
                                .build())
                em.persist(
                        AdoptionPosts.builder()
                                .adoptionPostId('AP_' + user.getUserId() + i)
                                .title('title' + user.getUserId() + i)
                                .contents('contents' + user.getUserId() + i)
                                .user(user)
                                .build())
            }
        }

        when:
        def result = usersRepository.searchShelters(new SearchShelterCondition(cityNameCond, shelterName, assessmentStatus), PageRequest.of(0, 5))

        then:
        result.getTotalElements() == totalElement
        result.getContent().size() == size
        result.getContent().get(0) instanceof ShelterPageResponseDto
        result.getContent().get(0).getUserId() != null
        result.getContent().get(0).getAllFeedCount() == 9
        result.getContent().get(0).getAssessmentStatus() == assessmentStatus
        result.getContent().get(0).getShelterName().contains('보호소')
        result.getContent().get(0).getShelterAddress().contains('헬로우 월드')
        result.getContent().get(0).getShelterPhoneNumber().contains('12-1234')
        result.getContent().get(0).getShelterHomePage().contains('.com')
        result.getContent().get(0).getProfileImage() == profileImage
        result.getTotalPages() == totalPages

        where:
        testName               | cityNameCond | shelterName | profileImage   | assessmentStatus           | totalElement | totalPages | size
        '검색조건이 없는 경우'          | null         | null        | 'profileImage' | AssessmentStatus.PENDING   | 20           | 4          | 5
        '지역명으로 검색하는 경우'        | '서울특별시'      | null        | 'profileImage' | AssessmentStatus.PENDING   | 10           | 2          | 5
        '보호소명으로 검색하는 경우'       | null         | 'Hulk10'    | 'profileImage' | AssessmentStatus.COMPLETED | 1            | 1          | 1
        '지역명 + 보호소명으로 검색하는 경우' | '서울특별시'      | 'Hulk10'    | 'profileImage' | AssessmentStatus.COMPLETED | 1            | 1          | 1
    }

    @Unroll(value = "#testName")
    def "보호소 리스트 취득 (데이터가 없는 경우)"() {
        when:
        def result = usersRepository.searchShelters(new SearchShelterCondition(cityNameCond, shelterName, null), PageRequest.of(0, 5))

        then:
        result.getTotalPages() == 0
        result.getTotalElements() == 0
        result.getContent().size() == 0

        where:
        testName               | cityNameCond | shelterName
        '검색조건이 없는 경우'          | null         | null
        '지역명으로 검색하는 경우'        | '서울특별시'      | null
        '보호소명으로 검색하는 경우'       | null         | 'Hulk10'
        '지역명 + 보호소명으로 검색하는 경우' | '부산'         | 'Hulk10'
    }

    @Unroll("#testCase")
    def "일반유저 디테일 조회"() {
        given:
        usersRepository.save(
                Users.builder()
                        .userId(userId)
                        .loginMethod(loginMethod)
                        .uid(uid)
                        .name(name)
                        .userType(userType)
                        .build())

        em.flush()
        em.clear()

        when:
        Users normalUser = usersRepository.findNormalUserByUserId(userId)

        then:
        if (userType == UserType.NORMAL) {
            normalUser != null
            normalUser.getUserId() == userId
            normalUser.getLoginMethod() == loginMethod
            normalUser.getUid() == uid
            normalUser.getName() == name
            normalUser.getUserType() == userType
        } else {
            normalUser == null
        }

        where:
        testCase      | userId   | loginMethod       | uid   | name   | userType
        '일반유저인 경우'    | 'userId' | LoginMethod.APPLE | 'uid' | 'name' | UserType.NORMAL
        '일반유저가 아닌 경우' | 'userId' | LoginMethod.APPLE | 'uid' | 'name' | UserType.SHELTER
    }

    def "보호소 디테일 조회"() {
        given:
        usersRepository.save(
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
                        .build())

        em.flush()
        em.clear()

        when:
        Users shelter = usersRepository.findShelterByUserId(userId)

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
        shelter.getShelterAccount() == shelterAccount
        shelter.getShelterIntro() == shelterIntro

        where:
        userId   | loginMethod       | uid   | name   | userType         | shelterName | shelterAddress                 | shelterIntro   | shelterAccount   | shelterAssessmentStatus
        'userId' | LoginMethod.APPLE | 'uid' | 'name' | UserType.SHELTER | '토르 보호소'    | '서울특별시 헬로우 월드 주소 어디서나 123-123' | 'shelterIntro' | 'shelterAccount' | AssessmentStatus.PENDING
    }

    def "유저 디테일 조회"() {
        given:
        def user = usersRepository.save(
                Users.builder()
                        .userId(userId)
                        .loginMethod(loginMethod)
                        .uid(uid)
                        .name(name)
                        .intro(intro)
                        .address(address)
                        .profileImage(profileImage)
                        .userType(userType)
                        .build())

        def normalPost = normalPostsRepository.save(
                NormalPosts.builder()
                        .normalPostId('normalPostId')
                        .title('title')
                        .contents('content')
                        .user(user)
                        .build())

        LongStream.range(0, allReplyCount).forEach(idx -> {
            normalReplyRepository.save(
                    NormalReply.builder()
                            .normalReplyId('replyId' + idx)
                            .normalPost(normalPost)
                            .user(user)
                            .content('content')
                            .build())
        })

        em.flush()
        em.clear()

        when:
        UserDetailResponseDto userDetailResponseDto = usersRepository.findNormalUserDetailByUserId(userId)

        then:
        userDetailResponseDto != null
        userDetailResponseDto.getUserId() == userId
        userDetailResponseDto.getUserName() == name
        userDetailResponseDto.getEmail() == null
        userDetailResponseDto.getIntro() == intro
        userDetailResponseDto.getAddress() == address
        userDetailResponseDto.getProfileImage() == profileImage
        userDetailResponseDto.getAllReplyCount() == allReplyCount

        where:
        userId   | loginMethod       | uid   | name   | userType        | allReplyCount | intro   | address   | profileImage
        'userId' | LoginMethod.APPLE | 'uid' | 'name' | UserType.NORMAL | 3L            | 'intro' | 'address' | 'profileImage'
    }
}
