package com.leadpet.www.infrastructure.db.users.savedPost

import com.leadpet.www.TestConfig
import com.leadpet.www.infrastructure.db.posts.adoptionPost.AdoptionPostsRepository
import com.leadpet.www.infrastructure.db.posts.donationPost.DonationPostsRepository
import com.leadpet.www.infrastructure.db.posts.normalPost.NormalPostsRepository
import com.leadpet.www.infrastructure.db.users.UsersRepository
import com.leadpet.www.infrastructure.domain.posts.AdoptionPosts
import com.leadpet.www.infrastructure.domain.posts.NormalPosts
import com.leadpet.www.infrastructure.domain.posts.PostType
import com.leadpet.www.infrastructure.domain.users.LoginMethod
import com.leadpet.www.infrastructure.domain.users.savedPost.SavedPost
import com.leadpet.www.infrastructure.domain.users.UserType
import com.leadpet.www.infrastructure.domain.users.Users
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageRequest
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
    NormalPostsRepository normalPostsRepository
    @Autowired
    AdoptionPostsRepository adoptionPostsRepository
    @Autowired
    DonationPostsRepository donationPostsRepository
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

    def "저장한 일상 피드를 userId로 취득"() {
        given:
        def user = usersRepository.save(
                Users.builder()
                        .userId(userId)
                        .loginMethod(LoginMethod.APPLE)
                        .uid("uid")
                        .name('name')
                        .userType(UserType.NORMAL)
                        .build())

        normalPostsRepository.save(
                NormalPosts.builder()
                        .normalPostId(normalPostId)
                        .title(title)
                        .contents('contents')
                        .images(image)
                        .user(user)
                        .build())
        // 저장피드 추가
        savedPostRepository.save(
                SavedPost.builder()
                        .savedPostId('savedPostId')
                        .postId(normalPostId)
                        .postType(PostType.NORMAL_POST)
                        .user(user)
                        .build())

        when:
        def result = savedPostRepository.findSavedNormalPostsByUserId(userId, PageRequest.of(0, 5))

        then:
        result.getTotalElements() == 1
        result.getContent().get(0).getNormalPostId() == normalPostId
        result.getContent().get(0).getUserId() == userId
        result.getContent().get(0).getTitle() == title
        result.getContent().get(0).getImages() == image

        where:
        userId   | normalPostId   | title   | image
        'userId' | 'normalPostId' | 'title' | ['img1', 'img2']
    }

    def "저장한 입양 피드를 userId로 취득"() {
        given:
        def user = usersRepository.save(
                Users.builder()
                        .userId(userId)
                        .loginMethod(LoginMethod.APPLE)
                        .uid("uid")
                        .name('name')
                        .userType(UserType.NORMAL)
                        .build())

        adoptionPostsRepository.save(
                AdoptionPosts.builder()
                        .adoptionPostId(adoptionPostId)
                        .title(title)
                        .contents('contents')
                        .images(image)
                        .user(user)
                        .build())
        // 저장피드 추가
        savedPostRepository.save(
                SavedPost.builder()
                        .savedPostId('savedPostId')
                        .postId(adoptionPostId)
                        .postType(PostType.ADOPTION_POST)
                        .user(user)
                        .build())

        when:
        def result = savedPostRepository.findSavedAdoptionPostsByUserId(userId, PageRequest.of(0, 5))

        then:
        result.getTotalElements() == 1
        result.getContent().get(0).getAdoptionPostId() == adoptionPostId
        result.getContent().get(0).getUserId() == userId
        result.getContent().get(0).getTitle() == title
        result.getContent().get(0).getImages() == image

        where:
        userId   | adoptionPostId   | title   | image
        'userId' | 'adoptionPostId' | 'title' | ['img1', 'img2']
    }

    def "저장한 기부 피드를 userId로 취득"() {
        given:
        def user = usersRepository.save(
                Users.builder()
                        .userId(userId)
                        .loginMethod(LoginMethod.APPLE)
                        .uid("uid")
                        .name('name')
                        .userType(UserType.NORMAL)
                        .build())

        adoptionPostsRepository.save(
                AdoptionPosts.builder()
                        .adoptionPostId('adoptionPostId')
                        .title(title)
                        .contents('contents')
                        .images(image)
                        .user(user)
                        .build())

        // 저장피드 추가
        savedPostRepository.save(
                SavedPost.builder()
                        .savedPostId('savedPostId')
                        .postId(donationPostId)
                        .postType(PostType.DONATION_POST)
                        .user(user)
                        .build())

        when:
        def result = savedPostRepository.findSavedDonationPostsByUserId(userId, PageRequest.of(0, 5))

        then:
        result.getTotalElements() == 1
        result.getContent().get(0).getDonationPostId() == donationPostId
        result.getContent().get(0).getUserId() == userId
        result.getContent().get(0).getTitle() == title
        result.getContent().get(0).getImages() == image

        where:
        userId   | donationPostId   | title   | image
        'userId' | 'donationPostId' | 'title' | ['img1', 'img2']
    }

}
