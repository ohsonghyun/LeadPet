package com.leadpet.www.infrastructure.domain.reply.normal

import com.leadpet.www.TestConfig
import com.leadpet.www.infrastructure.db.normalPost.NormalPostsRepository
import com.leadpet.www.infrastructure.db.reply.normal.NormalReplyRepository
import com.leadpet.www.infrastructure.db.users.UsersRepository
import com.leadpet.www.infrastructure.domain.posts.NormalPosts
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
 * NormalReplySpec
 */
@DataJpaTest
@Import(TestConfig)
@Transactional
class NormalReplySpec extends Specification {

    @PersistenceContext
    EntityManager em

    @Autowired
    UsersRepository usersRepository
    @Autowired
    NormalPostsRepository normalPostsRepository
    @Autowired
    NormalReplyRepository normalReplyRepository

    def "댓글 내용 수정"() {
        given:
        // 일상 피드 생성
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
                        .uid("uid")
                        .userType(UserType.NORMAL)
                        .build())

        // 댓글 생성
        normalReplyRepository.save(
                NormalReply.builder()
                        .normalReplyId(replyId)
                        .normalPost(normalPost)
                        .user(user)
                        .content(oldReplyContent)
                        .build()
        )
        em.flush()
        em.clear()

        when:
        def normalReply = normalReplyRepository.findById(replyId).orElseThrow()
        normalReply.updateContent(newReplyContent)
        em.flush()
        em.clear()

        then:
        def updatedReply = normalReplyRepository.findById(replyId).orElseThrow()
        updatedReply.getNormalPost().getNormalPostId() == postId
        updatedReply.getContent() == newReplyContent
        updatedReply.getUser().getUserId() == userId
        updatedReply.getUser().getName() == userName

        where:
        replyId   | postId   | postTitle | postContent     | userId   | userName   | oldReplyContent     | newReplyContent
        'replyId' | 'postId' | 'title'   | 'post contents' | 'userId' | 'userName' | 'old reply content' | 'new reply content'
    }

}
