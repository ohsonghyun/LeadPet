package com.leadpet.www.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.leadpet.www.application.service.reply.normal.NormalReplyService
import com.leadpet.www.infrastructure.domain.posts.NormalPosts
import com.leadpet.www.infrastructure.domain.reply.normal.NormalReply
import com.leadpet.www.infrastructure.domain.users.Users
import com.leadpet.www.infrastructure.exception.PostNotFoundException
import com.leadpet.www.infrastructure.exception.login.UserNotFoundException
import com.leadpet.www.presentation.dto.request.reply.normal.AddNormalReplyRequestDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification

import static org.mockito.ArgumentMatchers.isA
import static org.mockito.Mockito.when
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
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

    def "[일상피드 댓글 추가] 존재하지 않는 일상피드 에러"() {
        given:
        when(normalReplyService.saveReply(isA(String.class), isA(String.class), isA(String.class)))
                .thenThrow(new PostNotFoundException("Error: 존재하지 않는 일상 피드"))

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
                .andExpect(jsonPath('\$.error.detail').value('Error: 존재하지 않는 일상 피드'))
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
//    def "[일상피드 댓글 삭제] 존재하지 않는 댓글ID"() {
//        // TODO
//    }
//    def "[일상피드 댓글 삭제] 삭제 권한이 없는 유저"() {
//        // TODO
//    }
//    def "[일상피드 댓글 수정] 존재하지 않는 댓글ID"() {
//        // TODO
//    }
//    def "[일상피드 댓글 수정] 수정 권한이 없는 유저"() {
//        // TODO
//    }
}
