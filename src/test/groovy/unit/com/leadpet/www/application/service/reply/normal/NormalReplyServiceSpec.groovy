package com.leadpet.www.application.service.reply.normal

import com.leadpet.www.infrastructure.db.normalPost.NormalPostsRepository
import com.leadpet.www.infrastructure.db.reply.NormalReplyRepository
import com.leadpet.www.infrastructure.db.users.UsersRepository
import com.leadpet.www.infrastructure.domain.posts.NormalPosts
import com.leadpet.www.infrastructure.domain.reply.normal.NormalReply
import com.leadpet.www.infrastructure.domain.users.Users
import com.leadpet.www.infrastructure.exception.PostNotFoundException
import com.leadpet.www.infrastructure.exception.login.UserNotFoundException
import spock.lang.Specification

/**
 * NormalReplyServiceSpec
 */
class NormalReplyServiceSpec extends Specification {

    private UsersRepository usersRepository
    private NormalPostsRepository normalPostsRepository
    private NormalReplyRepository normalReplyRepository
    private NormalReplyService normalReplyService

    def setup() {
        usersRepository = Mock(UsersRepository)
        normalPostsRepository = Mock(NormalPostsRepository)
        normalReplyRepository = Mock(NormalReplyRepository)
        normalReplyService = new NormalReplyService(usersRepository, normalPostsRepository, normalReplyRepository)
    }

    def "새로운 일상피드 댓글 추가"() {
        given:
        normalPostsRepository.findById(_ as String) >> Optional.of(
                NormalPosts.builder()
                        .normalPostId(normalPostId)
                        .title('title')
                        .contents('content')
                        .build())

        usersRepository.findByUserId(_ as String) >> Users.builder().build()

        normalReplyRepository.save(_ as NormalReply) >>
                NormalReply.builder()
                        .normalReplyId(replyId)
                        .userId(userId)
                        .content(replyContent)
                        .build()

        when:
        def savedReply = normalReplyService.saveReply(normalPostId, userId, replyContent)

        then:
        savedReply.getNormalReplyId() == replyId
        savedReply.getUserId() == userId
        savedReply.getContent() == replyContent

        where:
        replyId   | userId   | normalPostId   | replyContent
        'replyId' | 'userId' | 'normalPostId' | 'replyContent'
    }

    def "존재하지 않는 일상피드ID인 경우 에러"() {
        given:
        normalPostsRepository.findById(_ as String) >> Optional.empty()

        when:
        normalReplyService.saveReply(normalPostId, userId, replyContent)

        then:
        thrown(PostNotFoundException)

        where:
        replyId   | userId   | normalPostId   | replyContent
        'replyId' | 'userId' | 'normalPostId' | 'replyContent'
    }

    def "존재하지 않는 유저ID인 경우 에러"() {
        given:
        normalPostsRepository.findById(_ as String) >> Optional.of(
                NormalPosts.builder()
                        .normalPostId(normalPostId)
                        .title('title')
                        .contents('content')
                        .build())

        usersRepository.findByUserId(_ as String) >> null

        when:
        normalReplyService.saveReply(normalPostId, userId, replyContent)

        then:
        thrown(UserNotFoundException)

        where:
        replyId   | userId   | normalPostId   | replyContent
        'replyId' | 'userId' | 'normalPostId' | 'replyContent'
    }
}
