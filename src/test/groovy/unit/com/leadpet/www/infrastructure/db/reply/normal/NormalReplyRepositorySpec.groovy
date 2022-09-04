package com.leadpet.www.infrastructure.db.reply.normal

import com.leadpet.www.TestConfig
import com.leadpet.www.infrastructure.db.posts.normalPost.NormalPostsRepository
import com.leadpet.www.infrastructure.db.users.UsersRepository
import com.leadpet.www.infrastructure.domain.posts.NormalPosts
import com.leadpet.www.infrastructure.domain.reply.normal.NormalReply
import com.leadpet.www.infrastructure.domain.users.LoginMethod
import com.leadpet.www.infrastructure.domain.users.UserType
import com.leadpet.www.infrastructure.domain.users.Users
import com.leadpet.www.presentation.dto.response.reply.normal.NormalReplyPageResponseDto
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
 * NormalReplyRepositorySpec
 */
@DataJpaTest
@Import(TestConfig)
@Transactional
class NormalReplyRepositorySpec extends Specification {

    @PersistenceContext
    EntityManager em

    @Autowired
    UsersRepository usersRepository
    @Autowired
    NormalPostsRepository normalPostsRepository
    @Autowired
    NormalReplyRepository normalReplyRepository

    def "일상 피드에 댓글을 추가"() {
        given:
        // 일상피드 생성
        def normalPost = NormalPosts.builder()
                .normalPostId(postId)
                .title(postTitle)
                .contents(postContent)
                .build()
        normalPostsRepository.save(normalPost)

        // 유저 생성
        def user = usersRepository.save(
                Users.builder()
                        .userId(userId)
                        .loginMethod(LoginMethod.KAKAO)
                        .name('userName')
                        .uid('uid')
                        .userType(UserType.NORMAL)
                        .profileImage(profileImage)
                        .build())

        // 댓글 생성
        def normalReply = NormalReply.builder()
                .normalReplyId(replyId)
                .normalPost(normalPost)
                .user(user)
                .content(replyContent)
                .build()

        when:
        normalReplyRepository.save(normalReply)
        em.flush()
        em.clear()

        then:
        def result = normalReplyRepository.findById(replyId).orElseThrow()
        result.getNormalReplyId() == replyId
        result.getNormalPost().getNormalPostId() == postId
        result.getUser().getUserId() == userId
        result.getUser().getProfileImage() == profileImage
        result.getContent() == replyContent

        where:
        replyId   | postId   | postTitle | postContent     | userId   | replyContent    | profileImage
        'replyId' | 'postId' | 'title'   | 'post contents' | 'userId' | 'reply content' | 'profileImage'
    }

    def "일상 피드 댓글 삭제"() {
        given:
        def normalPost = NormalPosts.builder()
                .normalPostId(postId)
                .title(postTitle)
                .contents(postContent)
                .build()
        normalPostsRepository.save(normalPost)

        // 유저 생성
        def user = usersRepository.save(
                Users.builder()
                        .userId(userId)
                        .loginMethod(LoginMethod.KAKAO)
                        .name('userName')
                        .uid('uid')
                        .userType(UserType.NORMAL)
                        .build())

        normalReplyRepository.save(
                NormalReply.builder()
                        .normalReplyId(replyId)
                        .normalPost(normalPost)
                        .user(user)
                        .content(replyContent)
                        .build())
        em.flush()
        em.clear()

        when:
        normalReplyRepository.deleteById(replyId)
        em.flush()
        em.clear()

        then:
        def result = normalReplyRepository.findById(replyId)
        result.isEmpty()

        where:
        replyId   | postId   | postTitle | postContent     | userId   | replyContent
        'replyId' | 'postId' | 'title'   | 'post contents' | 'userId' | 'reply content'
    }

    @Unroll(value = "#testCase")
    def "일상피드ID로 댓글 조회"() {
        given:
        def normalPost = NormalPosts.builder()
                .normalPostId(postId)
                .title(postTitle)
                .contents(postContent)
                .build()
        normalPostsRepository.save(normalPost)

        // 유저 생성
        def user = usersRepository.save(
                Users.builder()
                        .userId(userId)
                        .loginMethod(LoginMethod.KAKAO)
                        .name(userName)
                        .uid('uid')
                        .userType(userType)
                        .profileImage(profileImage)
                        .build())

        IntStream.range(0, size).forEach(idx -> {
            normalReplyRepository.save(
                    NormalReply.builder()
                            .normalReplyId(replyId + idx)
                            .normalPost(normalPost)
                            .user(user)
                            .content(replyContent)
                            .build())
        })
        em.flush()
        em.clear()

        when:
        def replies = normalReplyRepository.findByPostId(postId, PageRequest.of(0, 5))

        then:
        replies != null
        replies.getContent().size() == size
        replies.getTotalPages() == totalPage
        replies.getSize() == 5
        if (size > 0) {
            replies.getContent().get(0) instanceof NormalReplyPageResponseDto
            replies.getContent().get(0).getUserId() == userId
            replies.getContent().get(0).getUserName() == userName
            replies.getContent().get(0).getContent() == replyContent
            replies.getContent().get(0).getUserType() == userType
            replies.getContent().get(0).getUserProfileImage() == profileImage
        }

        where:
        testCase     | replyId   | postId   | postTitle | postContent     | userId   | userName   | profileImage   | replyContent    | userType        | size | totalPage
        '데이터가 있는 경우' | 'replyId' | 'postId' | 'title'   | 'post contents' | 'userId' | 'userName' | 'profileImage' | 'reply content' | UserType.NORMAL | 5    | 1
        '데이터가 없는 경우' | 'replyId' | 'postId' | 'title'   | 'post contents' | 'userId' | 'userName' | 'profileImage' | 'reply content' | UserType.NORMAL | 0    | 0
    }

}