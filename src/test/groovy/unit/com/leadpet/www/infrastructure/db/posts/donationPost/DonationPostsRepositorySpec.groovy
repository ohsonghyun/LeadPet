package com.leadpet.www.infrastructure.db.posts.donationPost

import com.leadpet.www.TestConfig
import com.leadpet.www.infrastructure.db.posts.donationPost.condition.SearchDonationPostCondition
import com.leadpet.www.infrastructure.db.users.UsersRepository
import com.leadpet.www.infrastructure.domain.donation.DonationMethod
import com.leadpet.www.infrastructure.domain.posts.DonationPosts
import com.leadpet.www.infrastructure.domain.users.*
import org.assertj.core.api.Assertions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageRequest
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification
import spock.lang.Unroll

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import java.time.LocalDateTime
import java.util.stream.IntStream

/**
 * DonationPostsRepositorySpec
 */
@DataJpaTest
@Import(TestConfig.class)
@Transactional
class DonationPostsRepositorySpec extends Specification {

    @PersistenceContext
    EntityManager em

    @Autowired
    UsersRepository usersRepository

    @Autowired
    DonationPostsRepository donationPostsRepository

    @Unroll('#testcase')
    def "기부 피드 페이지네이션: 데이터가 있는 경우"() {
        given:
        // 유저 app0, app1 추가
        createUsers()

        // 피드 데이터 추가
        for (int i = 0; i < totalNumOfPosts; i++) {
            donationPostsRepository.save(
                    DonationPosts.builder()
                            .donationPostId('postId' + i)
                            .title('title')
                            .contents('contents')
                            .images(['img1', 'img2'])
                            .startDate(startDate)
                            .endDate(endDate)
                            .user(usersRepository.findShelterByUserId(i % 2 == 0 ? 'app0' : 'app1'))
                            .build())
        }

        em.flush()
        em.clear()

        when:
        final result = donationPostsRepository.searchAll(
                SearchDonationPostCondition.builder().userId(targetUserId).build(),
                PageRequest.of(0, 5))

        then:
        result.getTotalElements() == expectedNumOfPosts

        def it = Assertions.assertThat(result.getContent())
        it.hasSize(5)
        it.extractingResultOf('getTitle').containsExactly('title', 'title', 'title', 'title', 'title')
        it.extractingResultOf('getUserName').containsAnyOf('name0', 'name1')

        where:
        testcase          | totalNumOfPosts | startDate           | endDate                | targetUserId | expectedNumOfPosts
        '검색조건이 null'      | 20              | LocalDateTime.now() | startDate.plusDays(10) | null         | 20
        '검색 userId가 공백'   | 20              | LocalDateTime.now() | startDate.plusDays(10) | ''           | 20
        '검색 userId가 app0' | 20              | LocalDateTime.now() | startDate.plusDays(10) | 'app0'       | 10
        '검색 userId가 app1' | 20              | LocalDateTime.now() | startDate.plusDays(10) | 'app1'       | 10
    }

    def "기부 피드 페이지네이션: 데이터가 없는 경우"() {
        when:
        final result = donationPostsRepository.searchAll(
                SearchDonationPostCondition.builder().build(),
                PageRequest.of(0, 5))

        then:
        result.getContent().size() == 0
        result.getTotalElements() == 0
    }

    def "기부 피드 페이지네이션: 기부 품목(카테고리) 조건"() {
        given: '기부 피드 등록'
        // 유저 app0, app1 추가
        createUsers()

        def donationMethods = DonationMethod.values()

        // 피드 데이터 추가
        for (int i = 0; i < donationMethods.size(); i++) {
            donationPostsRepository.save(
                    DonationPosts.builder()
                            .donationPostId('postId' + i)
                            .title('title')
                            .contents('contents')
                            .images(['img1', 'img2'])
                            .startDate(startDate)
                            .endDate(endDate)
                            .donationMethod(donationMethods[i])
                            .user(usersRepository.findShelterByUserId(i % 2 == 0 ? 'app0' : 'app1'))
                            .build())
        }

        expect: '기부 페이지네이션 with DonationMethod'
        for (int i = 0; i < donationMethods.size(); i++) {
            def result = donationPostsRepository.searchAll(
                    SearchDonationPostCondition.builder().donationMethod(donationMethods[i]).build(),
                    PageRequest.of(0, 5))

            assert result.getContent().size() == expectedNumOfPosts
        }

        where:
        startDate           | endDate                | expectedNumOfPosts
        LocalDateTime.now() | startDate.plusDays(10) | 1
    }

    // -------------------------------------------------------------------
    private createUsers() {
        IntStream.range(0, 2).forEach(idx -> {
            usersRepository.save(
                    Users.builder()
                            .userId("app" + idx)
                            .loginMethod(LoginMethod.APPLE)
                            .uid("uid" + idx)
                            .name('name' + idx)
                            .userType(UserType.SHELTER)
                            .shelterInfo(
                                    ShelterInfo.builder()
                                            .shelterName("보호소" + idx)
                                            .shelterAddress("헬로우 월드 주소 어디서나 123-123")
                                            .shelterAssessmentStatus(AssessmentStatus.COMPLETED)
                                            .build()
                            )
                            .build()
            )
        })
    }
}
