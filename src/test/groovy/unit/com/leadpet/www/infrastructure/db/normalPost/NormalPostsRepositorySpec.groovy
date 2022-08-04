package com.leadpet.www.infrastructure.db.normalPost

import com.leadpet.www.TestConfig
import com.leadpet.www.infrastructure.db.normalPost.condition.SearchNormalPostCondition
import com.leadpet.www.infrastructure.db.users.UsersRepository
import com.leadpet.www.infrastructure.domain.posts.NormalPosts
import com.leadpet.www.infrastructure.domain.users.AssessmentStatus
import com.leadpet.www.infrastructure.domain.users.LoginMethod
import com.leadpet.www.infrastructure.domain.users.UserType
import com.leadpet.www.infrastructure.domain.users.Users
import com.leadpet.www.presentation.dto.response.post.NormalPostResponse
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

/**
 * NormalPostsRepositorySpec
 */
@DataJpaTest
@Import(TestConfig.class)
@Transactional
class NormalPostsRepositorySpec extends Specification {

    @PersistenceContext
    EntityManager em;

    @Autowired
    UsersRepository usersRepository
    @Autowired
    NormalPostsRepository normalPostsRepository

    @Unroll("#testcase")
    def "일반 피드 페이지네이션: 데이터가 있는 경우"() {
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
            normalPostsRepository.save(
                    NormalPosts.builder()
                            .normalPostId('postId' + i)
                            .title('title')
                            .contents('contents')
                            .user(usersRepository.findShelterByUserId(i % 2 ? 'app0' : 'app1'))
                            .build()
            )
        }

        em.flush()
        em.clear()

        when:
        def result = normalPostsRepository.searchAll(
                SearchNormalPostCondition.builder().userId(targetUserId).build(),
                PageRequest.of(0, 5))

        then:
        result.getContent().size() == 5
        result.getContent().get(0) instanceof NormalPostResponse
        result.getContent().get(0).getUserId() != null
        result.getContent().get(0).getNormalPostId() != null
        result.getContent().get(0).getImages().isEmpty()
        result.getContent().get(0).getContents() != null
        result.getContent().get(0).getTitle() != null
        result.getContent().get(0).getUserId() != null
        result.getContent().get(0).getLikedCount() == 0
        // TODO createdTime 도 테스트 하고 싶은데 DI 가 안 되는 듯.. 어떻게해야하지?
        result.getTotalElements() == expectedNumOfPosts

        where:
        testcase          | totalNumOfPosts | targetUserId | expectedNumOfPosts
        '검색 userId가 공백'   | 20              | ''           | 20
        '검색 userId가 null' | 20              | null         | 20
        '검색 userId가 app0' | 20              | 'app0'       | 10
        '검색 userId가 app1' | 20              | 'app1'       | 10
    }

    def "일반 피드 페이지네이션: 데이터가 없는 경우"() {
        when:
        def result = normalPostsRepository.searchAll(SearchNormalPostCondition.builder().build(), PageRequest.of(0, 5))

        then:
        result.getContent().size() == 0
        result.getTotalPages() == 0
        result.getTotalElements() == 0
    }
}
