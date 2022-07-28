package com.leadpet.www.infrastructure.db.reply.normal

import com.leadpet.www.TestConfig
import com.leadpet.www.infrastructure.db.normalPost.NormalPostsRepository
import com.leadpet.www.infrastructure.db.reply.NormalReplyRepository
import com.leadpet.www.infrastructure.db.users.UsersRepository
import com.leadpet.www.infrastructure.domain.posts.NormalPosts
import com.leadpet.www.infrastructure.domain.reply.normal.NormalReply
import com.leadpet.www.infrastructure.domain.users.LoginMethod
import com.leadpet.www.infrastructure.domain.users.UserType
import com.leadpet.www.infrastructure.domain.users.Users
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

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
        result.getContent() == replyContent

        where:
        replyId   | postId   | postTitle | postContent     | userId   | replyContent
        'replyId' | 'postId' | 'title'   | 'post contents' | 'userId' | 'reply content'
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

}