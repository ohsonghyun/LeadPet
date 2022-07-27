package com.leadpet.www.infrastructure.domain.reply.normal

import com.leadpet.www.TestConfig
import com.leadpet.www.infrastructure.db.normalPost.NormalPostsRepository
import com.leadpet.www.infrastructure.db.reply.NormalReplyRepository
import com.leadpet.www.infrastructure.domain.posts.NormalPosts
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

        // 댓글 생성
        normalReplyRepository.save(
                NormalReply.builder()
                        .normalReplyId(replyId)
                        .normalPost(normalPost)
                        .userId(userId)
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
        updatedReply.getUserId() == userId
        updatedReply.getNormalPost().getNormalPostId() == postId
        updatedReply.getContent() == newReplyContent

        where:
        replyId   | postId   | postTitle | postContent     | userId   | oldReplyContent     | newReplyContent
        'replyId' | 'postId' | 'title'   | 'post contents' | 'userId' | 'old reply content' | 'new reply content'
    }

}
