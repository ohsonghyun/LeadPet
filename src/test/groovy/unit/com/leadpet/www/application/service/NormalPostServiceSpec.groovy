package com.leadpet.www.application.service

import com.leadpet.www.infrastructure.db.posts.normalPost.NormalPostsRepository
import com.leadpet.www.infrastructure.db.posts.normalPost.condition.SearchNormalPostCondition
import com.leadpet.www.infrastructure.db.users.UsersRepository
import com.leadpet.www.infrastructure.domain.posts.NormalPosts
import com.leadpet.www.infrastructure.domain.users.LoginMethod
import com.leadpet.www.infrastructure.domain.users.UserType
import com.leadpet.www.infrastructure.domain.users.Users
import com.leadpet.www.infrastructure.exception.login.UserNotFoundException
import com.leadpet.www.presentation.dto.response.post.NormalPostResponse
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import spock.lang.Specification

import static org.assertj.core.api.Assertions.assertThat

/**
 * NormalPostServiceSpec
 */
class NormalPostServiceSpec extends Specification {

    private NormalPostService normalPostService
    private NormalPostsRepository normalPostsRepository
    private UsersRepository usersRepository

    def setup() {
        this.usersRepository = Mock(UsersRepository.class)
        this.normalPostsRepository = Mock(NormalPostsRepository.class)
        this.normalPostService = new NormalPostService(usersRepository, normalPostsRepository)
    }

    def "일반게시물 데이터를 페이징을 통해 취득한다"() {
        given:
        normalPostsRepository.searchAll(_ as SearchNormalPostCondition, _ as Pageable) >> new PageImpl(
                List.of(
                        NormalPostResponse.builder().normalPostId("NP_a").title("title").contents("contents").userId('user').likedCount(3).build(),
                        NormalPostResponse.builder().normalPostId("NP_b").title("title").contents("contents").userId('user').likedCount(2).build(),
                        NormalPostResponse.builder().normalPostId("NP_c").title("title").contents("contents").userId('user').likedCount(1).build()
                ),
                pageable,
                total
        )

        when:
        final result = normalPostService.getNormalPostsWith(
                SearchNormalPostCondition.builder().build(),
                pageable)

        then:
        result.getTotalElements() == total
        result.getTotalPages() == 1

        def it = assertThat(result.getContent())
        it.hasSize(total)
        it.extractingResultOf('getTitle').isNotNull()
        it.extractingResultOf('getContents').isNotNull()
        it.extractingResultOf('getNormalPostId').containsExactly('NP_a', 'NP_b', 'NP_c')
        it.extractingResultOf('getLikedCount').containsExactly(3L, 2L, 1L)

        where:
        pageable             | total
        PageRequest.of(0, 3) | 3
    }

    def "일반 게시글 추가: 정상"() {
        given: 'mocking'
        Users user = Users.builder()
                .loginMethod(loginMethod)
                .uid(uid)
                .name(name)
                .userId(userId)
                .userType(userType)
                .build()

        usersRepository.findById(_ as String) >> Optional.of(user)
        normalPostsRepository.save(_ as NormalPosts) >> NormalPosts.builder().normalPostId("NP_a")
                .title(title)
                .contents(contents)
                .user(user)
                .build()

        when:
        final result = normalPostService.addNewPost(
                NormalPosts.builder()
                        .title(title)
                        .user(user)
                        .contents(contents)
                        .build(),
                userId)

        then:
        result != null
        result.getTitle() == title
        result.getContents() == contents
        result.getUser() != null
        result.getUser().getUserId() == userId
        result.getUser().getName() == name
        result.getUser().getUid() == uid
        result.getUser().getUserType() == userType
        result.getUser().getLoginMethod() == loginMethod

        where:
        uid        | name        | userId   | userType        | loginMethod       | title        | contents
        'dummyUid' | 'dummyName' | 'uidkko' | UserType.NORMAL | LoginMethod.KAKAO | 'dummyTitle' | 'dummyContnets'
    }

    def "일반 게시글 추가: 에러: 404 - 존재하지 않는 유저"() {
        given:
        // dummy Users object
        Users user = Users.builder().build()
        usersRepository.findById(_ as String) >> { userId -> throw new UserNotFoundException('Error: 존재하지 않는 유저') }

        when:
        normalPostService.addNewPost(
                NormalPosts.builder()
                        .title('title')
                        .user(user)
                        .contents('contents')
                        .build(),
                'userId')

        then:
        thrown(UserNotFoundException)
    }

}
