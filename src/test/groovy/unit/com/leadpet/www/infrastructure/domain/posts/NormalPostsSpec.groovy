package com.leadpet.www.infrastructure.domain.posts

import com.leadpet.www.TestConfig
import com.leadpet.www.infrastructure.db.adoptionPost.condition.SearchAdoptionPostCondition
import com.leadpet.www.infrastructure.db.normalPost.NormalPostsRepository
import com.leadpet.www.infrastructure.db.normalPost.condition.SearchNormalPostCondition
import com.leadpet.www.infrastructure.db.users.UsersRepository
import com.leadpet.www.infrastructure.domain.pet.AnimalType
import com.leadpet.www.infrastructure.domain.pet.Gender
import com.leadpet.www.infrastructure.domain.pet.Neutering
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
 * NormalPostsSpec
 */
@DataJpaTest
@Import(TestConfig.class)
@Transactional
class NormalPostsSpec extends Specification {

    @PersistenceContext
    EntityManager em;

    @Autowired
    UsersRepository usersRepository

    @Autowired
    NormalPostsRepository normalPostsRepository

    def "일반 게시글 추가"() {
        when:
        normalPostsRepository.save(
                NormalPosts.builder()
                        .normalPostId(postId)
                        .title(title)
                        .contents(contents)
                        .build()
        )
        em.flush()
        em.clear()

        then:
        final saved = normalPostsRepository.findById(postId).orElseThrow()
        saved != null
        saved.getNormalPostId() == postId
        saved.getTitle() == title

        where:
        postId     | title   | contents
        'NP_dummy' | 'title' | 'contents'
    }

    def "일반 게시글을 수정"() {
        given:
        final target = normalPostsRepository.save(
                NormalPosts.builder()
                        .normalPostId(postId)
                        .title(title)
                        .contents(contents)
                        .build()
        )

        when:
        target.update(
                NormalPosts.builder()
                        .title(newTitle)
                // .contents(contents) // null 은 업데이트 대상외
                        .images(images)
                        .build())
        em.flush()
        em.clear()

        then:
        final result = normalPostsRepository.findById(postId).orElseThrow()
        result.getNormalPostId() == postId
        result.getContents() == contents
        result.getImages() == images

        where:
        postId     | title   | contents   | newTitle   | images
        'NP_dummy' | 'title' | 'contents' | 'newTitle' | ['img1', 'img2']
    }

    def "일반 게시글 삭제"() {
        given:
        normalPostsRepository.save(
                NormalPosts.builder()
                        .normalPostId(postId)
                        .title(title)
                        .contents(contents)
                        .build()
        )
        em.flush()
        em.clear()

        when:
        normalPostsRepository.deleteById(postId)

        then:
        final saved = normalPostsRepository.findById(postId)
        saved.isEmpty()

        where:
        postId     | title   | contents
        'NP_dummy' | 'title' | 'contents'
    }


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
