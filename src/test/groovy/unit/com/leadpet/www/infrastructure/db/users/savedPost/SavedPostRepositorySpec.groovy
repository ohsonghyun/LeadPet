package com.leadpet.www.infrastructure.db.users.savedPost

import com.leadpet.www.TestConfig
import com.leadpet.www.infrastructure.db.users.UsersRepository
import com.leadpet.www.infrastructure.domain.posts.PostType
import com.leadpet.www.infrastructure.domain.users.LoginMethod
import com.leadpet.www.infrastructure.domain.users.savedPost.SavedPost
import com.leadpet.www.infrastructure.domain.users.UserType
import com.leadpet.www.infrastructure.domain.users.Users
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@DataJpaTest
@Import(TestConfig)
@Transactional
class SavedPostRepositorySpec extends Specification {

    @PersistenceContext
    EntityManager em

    @Autowired
    SavedPostRepository savedPostRepository
    @Autowired
    UsersRepository usersRepository

    def "새로운 [저장피드]를 추가"() {
        given:
        def user = usersRepository.save(
                Users.builder()
                        .userId(userId)
                        .uid('uid')
                        .loginMethod(LoginMethod.KAKAO)
                        .name('name')
                        .userType(UserType.NORMAL)
                        .build()
        )

        when:
        savedPostRepository.save(
                SavedPost.builder()
                        .savedPostId('savedPostId')
                        .postId('postId')
                        .postType(PostType.NORMAL_POST)
                        .user(user)
                        .build())
        em.flush()
        em.clear()

        then:
        def savedPost = savedPostRepository.findById('savedPostId').orElseThrow()
        savedPost != null
        savedPost.getSavedPostId() == savedPostId
        savedPost.getPostId() == postId
        savedPost.getUser().getUserId() == userId

        where:
        savedPostId   | postId   | userId
        'savedPostId' | 'postId' | 'userId'
    }

    def "저장피드를 삭제"() {
        given:
        def user = usersRepository.save(
                Users.builder()
                        .userId(userId)
                        .uid('uid')
                        .loginMethod(LoginMethod.KAKAO)
                        .name('name')
                        .userType(UserType.NORMAL)
                        .build()
        )

        savedPostRepository.save(
                SavedPost.builder()
                        .savedPostId('savedPostId')
                        .postId('postId')
                        .postType(PostType.NORMAL_POST)
                        .user(user)
                        .build())
        em.flush()
        em.clear()

        when:
        savedPostRepository.deleteById(savedPostId)
        em.flush()
        em.clear()

        then:
        savedPostRepository.findById(savedPostId).isEmpty()

        where:
        savedPostId   | postId   | userId
        'savedPostId' | 'postId' | 'userId'
    }

}
