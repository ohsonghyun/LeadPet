package com.leadpet.www.application.service.reply.normal

import com.leadpet.www.infrastructure.db.normalPost.NormalPostsRepository
import com.leadpet.www.infrastructure.db.reply.NormalReplyRepository
import com.leadpet.www.infrastructure.db.users.UsersRepository
import com.leadpet.www.infrastructure.domain.posts.NormalPosts
import com.leadpet.www.infrastructure.domain.reply.normal.NormalReply
import com.leadpet.www.infrastructure.domain.users.Users
import com.leadpet.www.infrastructure.exception.PostNotFoundException
import com.leadpet.www.infrastructure.exception.ReplyNotFoundException
import com.leadpet.www.infrastructure.exception.UnauthorizedUserException
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
                        .user(Users.builder().userId(userId).name(userName).build())
                        .content(replyContent)
                        .build()

        when:
        def savedReply = normalReplyService.saveReply(normalPostId, userId, replyContent)

        then:
        savedReply.getNormalReplyId() == replyId
        savedReply.getUser().getUserId() == userId
        savedReply.getUser().getName() == userName
        savedReply.getContent() == replyContent

        where:
        replyId   | userId   | userName   | normalPostId   | replyContent
        'replyId' | 'userId' | 'userName' | 'normalPostId' | 'replyContent'
    }

    def "[일상피드 댓글 추가] 존재하지 않는 일상피드ID인 경우 에러"() {
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

    def "[일상피드 댓글 추가] 존재하지 않는 유저ID인 경우 에러"() {
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

    def "[댓글 삭제] 성공"() {
        given:
        normalReplyRepository.findById(_ as String) >> Optional.of(
                NormalReply.builder()
                        .normalReplyId(replyId)
                        .user(Users.builder().userId(userId).build())
                        .content('replyContent')
                        .build())

        when:
        def deletedReplyId = normalReplyService.deleteReply(userId, replyId)

        then:
        deletedReplyId != null
        deletedReplyId == replyId

        where:
        replyId   | userId
        'replyId' | 'userId'
    }

    def "[댓글 삭제] 댓글이 존재 하지 않으면 에러"() {
        given:
        normalReplyRepository.findById(_ as String) >> Optional.empty()

        when:
        normalReplyService.deleteReply('userId', 'unknownReplyId')

        then:
        thrown(ReplyNotFoundException)
    }

    def "[댓글 삭제] 작성자가 아닌 제3자가 삭제하려면 에러"() {
        given:
        normalReplyRepository.findById(_ as String) >> Optional.of(
                NormalReply.builder()
                        .normalReplyId(replyId)
                        .user(Users.builder().userId(userId).build())
                        .content('replyContent')
                        .build())

        when:
        normalReplyService.deleteReply('wrongUserId', replyId)

        then:
        thrown(UnauthorizedUserException)

        where:
        replyId   | userId
        'replyId' | 'userId'
    }

    def "[댓글 수정] 성공"() {
        given:
        normalReplyRepository.findById(_ as String) >> Optional.of(
                NormalReply.builder()
                        .normalReplyId(replyId)
                        .user(Users.builder().userId(userId).build())
                        .content('oldContent')
                        .build())

        when:
        def updatedReply = normalReplyService.updateContent(userId, replyId, newContent)

        then:
        updatedReply.getNormalReplyId() == replyId
        updatedReply.getUser().getUserId() == userId
        updatedReply.getContent() == newContent

        where:
        userId   | replyId   | newContent
        'userId' | 'replyId' | 'newContent'
    }

    def "[댓글 수정] 존재하지 않는 댓글 에러"() {
        given:
        normalReplyRepository.findById(_ as String) >> Optional.empty()

        when:
        normalReplyService.updateContent(userId, replyId, newContent)

        then:
        thrown(ReplyNotFoundException)

        where:
        userId   | replyId   | newContent
        'userId' | 'replyId' | 'newContent'
    }

    def "[댓글 수정] 작성자가 아닌 유저가 댓글 수정하려면 에러"() {
        given:
        normalReplyRepository.findById(_ as String) >> Optional.of(
                NormalReply.builder()
                        .normalReplyId(replyId)
                        .user(Users.builder().userId(userId).build())
                        .content('replyContent')
                        .build())

        when:
        normalReplyService.updateContent('wrongUserId', replyId, 'newContent')

        then:
        thrown(UnauthorizedUserException)

        where:
        replyId   | userId
        'replyId' | 'userId'
    }
}
