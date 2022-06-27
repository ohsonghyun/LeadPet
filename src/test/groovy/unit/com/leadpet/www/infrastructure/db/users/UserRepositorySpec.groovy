package com.leadpet.www.infrastructure.db.users

import com.leadpet.www.TestConfig
import com.leadpet.www.infrastructure.db.users.condition.SearchShelterCondition
import com.leadpet.www.infrastructure.domain.posts.AdoptionPosts
import com.leadpet.www.infrastructure.domain.posts.DonationPosts
import com.leadpet.www.infrastructure.domain.posts.NormalPosts
import com.leadpet.www.infrastructure.domain.users.AssessmentStatus
import com.leadpet.www.infrastructure.domain.users.LoginMethod
import com.leadpet.www.infrastructure.domain.users.UserType
import com.leadpet.www.infrastructure.domain.users.Users
import com.leadpet.www.presentation.dto.response.user.ShelterPageResponseDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageRequest
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import java.util.stream.IntStream

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

    def "보호소 리스트 취득"() {
        given:
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
                            .shelterAssessmentStatus(AssessmentStatus.PENDING)
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
        def result = usersRepository.searchShelters(new SearchShelterCondition(), PageRequest.of(0, 5))

        then:
        result.getTotalElements() == 20
        result.getContent().size() == 5
        result.getContent().get(0) instanceof ShelterPageResponseDto
        result.getContent().get(0).getAllFeedCount() == 9
        result.getContent().get(0).getAssessmentStatus() == AssessmentStatus.PENDING
        result.getTotalPages() == 4
    }
}
