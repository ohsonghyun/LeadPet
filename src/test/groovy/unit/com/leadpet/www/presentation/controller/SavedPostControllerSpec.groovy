package com.leadpet.www.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.leadpet.www.application.service.SavedPostService
import com.leadpet.www.infrastructure.domain.posts.PostType
import com.leadpet.www.infrastructure.domain.users.Users
import com.leadpet.www.infrastructure.domain.users.savedPost.SavedPost
import com.leadpet.www.infrastructure.exception.PostNotFoundException
import com.leadpet.www.infrastructure.exception.SavedPostNotFoundException
import com.leadpet.www.infrastructure.exception.UnauthorizedUserException
import com.leadpet.www.infrastructure.exception.login.UserNotFoundException
import com.leadpet.www.presentation.dto.request.user.savedPost.AddSavedPostRequest
import com.leadpet.www.presentation.dto.request.user.savedPost.DeleteSavedPostRequest
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import spock.lang.Unroll

import static org.mockito.ArgumentMatchers.isA
import static org.mockito.Mockito.when
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * SavedPostControllerSpec
 */
@WebMvcTest(SavedPostController)
class SavedPostControllerSpec extends Specification {
    private static final String BASE_URL = '/v1/savedPost'

    @Autowired
    MockMvc mvc

    @MockBean
    SavedPostService savedPostService

    def "[저장피드 추가] 정상"() {
        given:
        when(savedPostService.save(isA(String.class), isA(PostType.class), isA(String.class)))
                .thenReturn(
                        SavedPost.builder()
                                .savedPostId(savedPostId)
                                .postId(postId)
                                .postType(postType)
                                .user(user)
                                .build())

        expect:
        mvc.perform(
                post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(
                                AddSavedPostRequest.builder()
                                        .postId(postId)
                                        .postType(postType)
                                        .userId(userId)
                                        .build()
                        ))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath('\$.savedPostId').value(savedPostId))

        where:
        savedPostId   | postId   | postType             | userId   | user
        'savedPostId' | 'postId' | PostType.NORMAL_POST | 'userId' | Users.builder().userId(userId).build()
    }

    @Unroll("#testCase")
    def "[저장피드 추가] 에러 케이스"() {
        given:
        when(savedPostService.save(isA(String.class), isA(PostType.class), isA(String.class)))
                .thenThrow(error)

        expect:
        mvc.perform(
                post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(
                                AddSavedPostRequest.builder()
                                        .postId('postId')
                                        .postType(PostType.NORMAL_POST)
                                        .userId('userId')
                                        .build()
                        ))
        )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("\$.error.message").value('NOT_FOUND'))

        where:
        testCase       | error
        '존재하지 않는 유저ID' | new UserNotFoundException()
        '존재하지 않는 피드ID' | new PostNotFoundException()
    }

    def "[저장피드 삭제] 정상"() {
        given:
        when(savedPostService.deleteById(isA(String.class), isA(String.class))).thenReturn(savedPostId)

        expect:
        mvc.perform(
                delete(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(
                                DeleteSavedPostRequest.builder()
                                        .savedPostId(savedPostId)
                                        .userId(userId)
                                        .build()
                        ))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath('\$.savedPostId').value(savedPostId))

        where:
        savedPostId   | postType             | userId   | user
        'savedPostId' | PostType.NORMAL_POST | 'userId' | Users.builder().userId(userId).build()
    }

    @Unroll("#testCase")
    def "[저장피드 삭제] 에러 케이스"() {
        given:
        when(savedPostService.save(isA(String.class), isA(PostType.class), isA(String.class)))
                .thenThrow(error)

        expect:
        mvc.perform(
                post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(
                                AddSavedPostRequest.builder()
                                        .postId('postId')
                                        .postType(PostType.NORMAL_POST)
                                        .userId('userId')
                                        .build()
                        ))
        )
                .andExpect(expectedStatus)
                .andExpect(jsonPath("\$.error.message").value(expectedErrorValue))

        where:
        testCase         | error                            | expectedStatus         | expectedErrorValue
        '존재하지 않는 유저ID'   | new UserNotFoundException()      | status().isNotFound()  | 'NOT_FOUND'
        '권한 없는 유저'       | new UnauthorizedUserException()  | status().isForbidden() | 'FORBIDDEN'
        '존재하지 않는 저장피드ID' | new SavedPostNotFoundException() | status().isNotFound()  | 'NOT_FOUND'
    }
}
