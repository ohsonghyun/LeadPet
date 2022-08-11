package com.leadpet.www.application.service

import com.leadpet.www.infrastructure.db.posts.PostUtil
import com.leadpet.www.infrastructure.db.posts.adoptionPost.AdoptionPostsRepository
import com.leadpet.www.infrastructure.db.posts.donationPost.DonationPostsRepository
import com.leadpet.www.infrastructure.db.posts.normalPost.NormalPostsRepository
import com.leadpet.www.infrastructure.db.users.UsersRepository
import com.leadpet.www.infrastructure.db.users.savedPost.SavedPostRepository
import com.leadpet.www.infrastructure.domain.posts.NormalPosts
import com.leadpet.www.infrastructure.domain.posts.PostType
import com.leadpet.www.infrastructure.domain.users.LoginMethod
import com.leadpet.www.infrastructure.domain.users.savedPost.SavedPost
import com.leadpet.www.infrastructure.domain.users.UserType
import com.leadpet.www.infrastructure.domain.users.Users
import com.leadpet.www.infrastructure.exception.PostNotFoundException
import com.leadpet.www.infrastructure.exception.SavedPostNotFoundException
import com.leadpet.www.infrastructure.exception.UnauthorizedUserException
import com.leadpet.www.infrastructure.exception.login.UserNotFoundException
import spock.lang.Specification

/**
 * SavedPostServiceSpec
 */
class SavedPostServiceSpec extends Specification {

    private UsersRepository usersRepository
    private SavedPostRepository savedPostRepository
    private SavedPostService savedPostService

    private PostUtil postUtil
    private NormalPostsRepository normalPostsRepository
    private AdoptionPostsRepository adoptionPostsRepository
    private DonationPostsRepository donationPostsRepository

    def setup() {
        usersRepository = Mock(UsersRepository)
        savedPostRepository = Mock(SavedPostRepository)

        normalPostsRepository = Mock(NormalPostsRepository)
        adoptionPostsRepository = Mock(AdoptionPostsRepository)
        donationPostsRepository = Mock(DonationPostsRepository)
        postUtil = new PostUtil(normalPostsRepository, donationPostsRepository, adoptionPostsRepository)

        savedPostService = new SavedPostService(usersRepository, savedPostRepository, postUtil)
    }

    def "[저장피드 추가] 정상"() {
        given:
        def user = Users.builder()
                .userId(userId)
                .uid('uid')
                .loginMethod(LoginMethod.KAKAO)
                .name('name')
                .userType(UserType.NORMAL)
                .build()

        usersRepository.findByUserId(_ as String) >> user
        postUtil.getRepositoryBy(_ as PostType) >> normalPostsRepository
        normalPostsRepository.findById(_ as String) >> Optional.of(
                NormalPosts.builder().build()
        )
        savedPostRepository.save(_ as SavedPost) >> SavedPost.builder()
                .savedPostId(savedPostId)
                .postId(postId)
                .postType(postType)
                .user(user)
                .build()

        when:
        def savedPost = savedPostService.save(postId, postType, userId)

        then:
        savedPost.getSavedPostId() == savedPostId
        savedPost.getPostId() == postId
        savedPost.getUser().getUserId() == userId
        savedPost.getPostType() == postType

        where:
        savedPostId   | postId   | userId   | postType
        'savedPostId' | 'postId' | 'userId' | PostType.NORMAL_POST
    }

    def "[저장피드 추가] 존재하지 않는 유저"() {
        when:
        savedPostService.save('postId', PostType.NORMAL_POST, 'userId')

        then:
        thrown(UserNotFoundException)
    }

    def "[저장피드 추가] 존재하지 않는 피드"() {
        given:
        usersRepository.findByUserId(_ as String) >> Users.builder().build()
        postUtil.getRepositoryBy(_ as PostType) >> normalPostsRepository
        normalPostsRepository.findById(_ as String) >> Optional.empty()

        when:
        savedPostService.save('postId', PostType.NORMAL_POST, 'userId')

        then:
        thrown(PostNotFoundException)
    }

    def "[저장피드 삭제] 정상"() {
        given:
        def user = Users.builder().userId(userId).build()
        usersRepository.findByUserId(_ as String) >> user
        savedPostRepository.findById(_ as String) >> Optional.of(SavedPost.builder().user(user).build())

        when:
        def result = savedPostService.deleteById(userId, savedPostId)

        then:
        result == savedPostId

        where:
        savedPostId   | userId
        'savedPostId' | 'userId'
    }

    def "[저장피드 삭제] 존재 하지 않는 유저"() {
        when:
        savedPostService.deleteById('userId', 'savedPostId')

        then:
        thrown(UserNotFoundException)
    }

    def "[저장피드 삭제] 존재 하지 않는 피드"() {
        given:
        usersRepository.findByUserId(_ as String) >> Users.builder().userId(userId).build()
        savedPostRepository.findById(_ as String) >> Optional.empty()

        when:
        savedPostService.deleteById(userId, savedPostId)

        then:
        thrown(SavedPostNotFoundException)

        where:
        savedPostId   | userId
        'savedPostId' | 'userId'
    }

    def "[저장피드 삭제] 권한 없는 유저"() {
        given:
        usersRepository.findByUserId(_ as String) >> Users.builder().userId(userId).build()
        savedPostRepository.findById(_ as String) >> Optional.of(
                SavedPost.builder()
                        .user(
                                Users.builder()
                                        .userId('otherUserId')
                                        .build()
                        )
                        .build()
        )

        when:
        savedPostService.deleteById(userId, savedPostId)

        then:
        thrown(UnauthorizedUserException)

        where:
        savedPostId   | userId
        'savedPostId' | 'userId'
    }

}
