package com.leadpet.www.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.leadpet.www.application.service.reply.normal.NormalReplyService
import com.leadpet.www.infrastructure.domain.posts.NormalPosts
import com.leadpet.www.infrastructure.domain.reply.normal.NormalReply
import com.leadpet.www.infrastructure.domain.users.UserType
import com.leadpet.www.infrastructure.domain.users.Users
import com.leadpet.www.infrastructure.exception.PostNotFoundException
import com.leadpet.www.infrastructure.exception.ReplyNotFoundException
import com.leadpet.www.infrastructure.exception.UnauthorizedUserException
import com.leadpet.www.infrastructure.exception.login.UserNotFoundException
import com.leadpet.www.presentation.dto.request.reply.normal.AddNormalReplyRequestDto
import com.leadpet.www.presentation.dto.request.reply.normal.DeleteNormalReplyRequestDto
import com.leadpet.www.presentation.dto.request.reply.normal.UpdateNormalReplyRequestDto
import com.leadpet.www.presentation.dto.response.reply.normal.NormalReplyPageResponseDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import spock.lang.Unroll

import static org.mockito.ArgumentMatchers.isA
import static org.mockito.Mockito.when
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * NormalReplyControllerSpec
 */
@WebMvcTest(NormalReplyController)
class NormalReplyControllerSpec extends Specification {
    private static final String BASE_URL = '/v1/reply/normal'

    @Autowired
    MockMvc mvc

    @Autowired
    ObjectMapper mapper

    @MockBean
    NormalReplyService normalReplyService

    def "[일상피드 댓글 추가] 정상"() {
        given:
        when(normalReplyService.saveReply(isA(String.class), isA(String.class), isA(String.class)))
                .thenReturn(
                        NormalReply.builder()
                                .normalReplyId(normalReplyId)
                                .user(Users.builder().userId(userId).name(userName).build())
                                .content(content)
                                .normalPost(NormalPosts.builder().build())
                                .build()
                )

        expect:
        mvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        mapper.writeValueAsString(
                                AddNormalReplyRequestDto.builder()
                                        .userId(userId)
                                        .content(content)
                                        .normalPostId(normalPostId)
                                        .build()
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath('\$.normalReplyId').value(normalReplyId))
                .andExpect(jsonPath('\$.userId').value(userId))
                .andExpect(jsonPath('\$.userName').value(userName))
                .andExpect(jsonPath('\$.content').value(content))
        // .andExpect(jsonPath('\$.createdDate').isNotEmpty())

        where:
        userId   | userName   | normalPostId   | content   | normalReplyId
        'userId' | 'userName' | 'normalPostId' | 'content' | 'normalReplyId'
    }

    @Unroll
    def "[일상피드 댓글 추가] #detail"() {
        given:
        when(normalReplyService.saveReply(isA(String.class), isA(String.class), isA(String.class)))
                .thenThrow(exception)

        expect:
        mvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        mapper.writeValueAsString(
                                AddNormalReplyRequestDto.builder()
                                        .userId('userId')
                                        .content('content')
                                        .normalPostId('unknownPostId')
                                        .build()
                        )))
                .andExpect(status)
                .andExpect(jsonPath('\$.error.code').value(code))
                .andExpect(jsonPath('\$.error.message').value(message))
                .andExpect(jsonPath('\$.error.detail').value(detail))

        where:
        detail                 | exception                         | status                | code | message
        "Error: 존재하지 않는 일상 피드" | new PostNotFoundException(detail) | status().isNotFound() | 404  | 'NOT_FOUND'
        "Error: 존재하지 않는 유저"    | new UserNotFoundException(detail) | status().isNotFound() | 404  | 'NOT_FOUND'
    }

    def "[일상피드 댓글 추가] 존재하지 않는 유저ID"() {
        given:
        when(normalReplyService.saveReply(isA(String.class), isA(String.class), isA(String.class)))
                .thenThrow(new UserNotFoundException(errorMessage))

        expect:
        mvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        mapper.writeValueAsString(
                                AddNormalReplyRequestDto.builder()
                                        .userId('userId')
                                        .content('content')
                                        .normalPostId('unknownPostId')
                                        .build()
                        )))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath('\$.error.code').value(404))
                .andExpect(jsonPath('\$.error.message').value('NOT_FOUND'))
                .andExpect(jsonPath('\$.error.detail').value(errorMessage))

        where:
        errorMessage << ['Error: 존재하지 않는 유저']
    }

    def "[일상피드 댓글 삭제] 정상"() {
        given:
        when(normalReplyService.deleteReply(isA(String.class), isA(String.class)))
                .thenReturn(normalReplyId)

        expect:
        mvc.perform(delete(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        mapper.writeValueAsString(
                                DeleteNormalReplyRequestDto.builder()
                                        .userId(userId)
                                        .normalReplyId(normalReplyId)
                                        .build()
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath('\$.normalReplyId').value(normalReplyId))

        where:
        userId   | normalReplyId
        'userId' | 'normalReplyId'
    }

    @Unroll
    def "[일상피드 댓글 삭제] #detail"() {
        given:
        when(normalReplyService.deleteReply(isA(String.class), isA(String.class)))
                .thenThrow(exception)

        expect:
        mvc.perform(delete(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        mapper.writeValueAsString(
                                DeleteNormalReplyRequestDto.builder()
                                        .userId('userId')
                                        .normalReplyId('normalReplyId')
                                        .build()
                        )))
                .andExpect(status)
                .andExpect(jsonPath('\$.error.code').value(code))
                .andExpect(jsonPath('\$.error.message').value(message))
                .andExpect(jsonPath('\$.error.detail').value(detail))

        where:
        detail                | exception                             | status                 | code | message
        "Error: 존재하지 않는 댓글"   | new ReplyNotFoundException(detail)    | status().isNotFound()  | 404  | 'NOT_FOUND'
        "Error: 삭제 권한이 없는 유저" | new UnauthorizedUserException(detail) | status().isForbidden() | 403  | 'FORBIDDEN'
    }

    def "[일상피드 댓글 수정] 정상"() {
        given:
        when(normalReplyService.updateContent(isA(String.class), isA(String.class), isA(String.class)))
                .thenReturn(
                        NormalReply.builder()
                                .normalReplyId(normalReplyId)
                                .user(Users.builder().userId(userId).name(userName).build())
                                .content(newContent)
                                .normalPost(NormalPosts.builder().build())
                                .build()
                )

        expect:
        mvc.perform(put(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        mapper.writeValueAsString(
                                UpdateNormalReplyRequestDto.builder()
                                        .userId(userId)
                                        .normalReplyId(normalReplyId)
                                        .newContent(newContent)
                                        .build()
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath('\$.normalReplyId').value(normalReplyId))
                .andExpect(jsonPath('\$.userId').value(userId))
                .andExpect(jsonPath('\$.userName').value(userName))
                .andExpect(jsonPath('\$.content').value(newContent))

        where:
        userId   | userName   | normalReplyId   | newContent
        'userId' | 'userName' | 'normalReplyId' | 'newContent'
    }

    @Unroll
    def "[일상피드 댓글 수정] #detail"() {
        given:
        when(normalReplyService.updateContent(isA(String.class), isA(String.class), isA(String.class)))
                .thenThrow(exception)

        expect:
        mvc.perform(put(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        mapper.writeValueAsString(
                                UpdateNormalReplyRequestDto.builder()
                                        .userId('userId')
                                        .normalReplyId('unknownReplyId')
                                        .newContent('newContent')
                                        .build()
                        )))
                .andExpect(status)
                .andExpect(jsonPath('\$.error.code').value(code))
                .andExpect(jsonPath('\$.error.message').value(message))
                .andExpect(jsonPath('\$.error.detail').value(detail))

        where:
        detail                | exception                             | status                 | code | message
        "Error: 존재하지 않는 댓글"   | new ReplyNotFoundException(detail)    | status().isNotFound()  | 404  | 'NOT_FOUND'
        "Error: 수정 권한이 없는 유저" | new UnauthorizedUserException(detail) | status().isForbidden() | 403  | 'FORBIDDEN'
    }

    def "[일상피드 댓글 페이지네이션] 정상"() {
        given:
        when(normalReplyService.findByPostId(isA(String.class), isA(Pageable.class)))
                .thenReturn(new PageImpl<NormalReplyPageResponseDto>(
                        List.of(
                                NormalReplyPageResponseDto.builder()
                                        .normalReplyId(replyId)
                                        .userId(userId)
                                        .userName(userName)
                                        .userProfileImage(userProfileImage)
                                        .userType(userType)
                                        .build()),
                        PageRequest.of(0, 5),
                        1
                ))

        expect:
        mvc.perform(get(BASE_URL + '?postId=dummyPost&page=0&size=5')
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath('\$.content.size()').value(1))
                .andExpect(jsonPath('\$.totalElements').value(1))
                .andExpect(jsonPath('\$.totalPages').value(1))
                .andExpect(jsonPath('\$.content[0].userId').value(userId))
                .andExpect(jsonPath('\$.content[0].userName').value(userName))
                .andExpect(jsonPath('\$.content[0].userProfileImage').value(userProfileImage))
                .andExpect(jsonPath('\$.content[0].userType').value(userType.name()))

        where:
        replyId   | userId   | userName   | userProfileImage   | userType
        'replyId' | 'userId' | 'userName' | 'userProfileImage' | UserType.NORMAL
    }

}
