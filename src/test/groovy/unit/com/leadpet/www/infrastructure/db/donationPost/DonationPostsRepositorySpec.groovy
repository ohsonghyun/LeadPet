package com.leadpet.www.infrastructure.db.donationPost

import com.leadpet.www.TestConfig
import com.leadpet.www.infrastructure.db.donationPost.condition.SearchDonationPostCondition
import com.leadpet.www.infrastructure.db.users.UsersRepository
import com.leadpet.www.infrastructure.domain.posts.DonationPosts
import com.leadpet.www.infrastructure.domain.users.AssessmentStatus
import com.leadpet.www.infrastructure.domain.users.LoginMethod
import com.leadpet.www.infrastructure.domain.users.UserType
import com.leadpet.www.infrastructure.domain.users.Users
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
        IntStream.range(0, 2).forEach(idx -> {
            usersRepository.save(
                    Users.builder()
                            .userId("app" + idx)
                            .loginMethod(LoginMethod.APPLE)
                            .uid("uid" + idx)
                            .name('name' + idx)
                            .userType(UserType.SHELTER)
                            .shelterName("보호소" + idx)
                            .shelterAddress("헬로우 월드 주소 어디서나 123-123")
                            .shelterAssessmentStatus(AssessmentStatus.COMPLETED)
                            .build()
            )
        })

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
        result.getContent().size() == 5
        result.getTotalElements() == expectedNumOfPosts

        where:
        testcase          | totalNumOfPosts | startDate           | endDate                | targetUserId | expectedNumOfPosts
        '검색 userId가 공백'   | 20              | LocalDateTime.now() | startDate.plusDays(10) | ''           | 20
        '검색 userId가 null' | 20              | LocalDateTime.now() | startDate.plusDays(10) | null         | 20
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
}
