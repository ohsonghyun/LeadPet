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
import com.leadpet.www.presentation.dto.response.post.adoption.SimpleAdoptionPostResponse
import com.leadpet.www.presentation.dto.response.post.donation.SimpleDonationPostResponse
import com.leadpet.www.presentation.dto.response.post.normal.SimpleNormalPostResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import spock.lang.Unroll

import static org.mockito.ArgumentMatchers.isA
import static org.mockito.Mockito.when
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
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

    def "[저장피드 취득] 일상피드 취득"() {
        given:
        when(savedPostService.findNormalPostByUserId(userId, PageRequest.of(0, 5)))
                .thenReturn(new PageImpl<SimpleNormalPostResponse>(
                        List.of(
                                SimpleNormalPostResponse.builder()
                                        .normalPostId(normalPostId)
                                        .title(title)
                                        .images(images)
                                        .userId(userId)
                                        .build()
                        )
                ))

        expect:
        mvc.perform(get(BASE_URL + '/normalPost/' + userId + "?page=0&size=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("\$.totalPages").value(1))
                .andExpect(jsonPath("\$.totalElements").value(1))
                .andExpect(jsonPath("\$.content[0].userId").value(userId))
                .andExpect(jsonPath("\$.content[0].normalPostId").value(normalPostId))
                .andExpect(jsonPath("\$.content[0].images").value(images))
                .andExpect(jsonPath("\$.content[0].title").value(title))

        where:
        userId   | images           | normalPostId   | title
        'userId' | ['img1', 'img2'] | 'normalPostId' | 'title'
    }

    def "[저장피드 취득] 입양피드 취득"() {
        given:
        when(savedPostService.findAdoptionPostByUserId(userId, PageRequest.of(0, 5)))
                .thenReturn(new PageImpl<SimpleAdoptionPostResponse>(
                        List.of(
                                SimpleAdoptionPostResponse.builder()
                                        .adoptionPostId(adoptionPostId)
                                        .title(title)
                                        .images(images)
                                        .userId(userId)
                                        .build()
                        )
                ))

        expect:
        mvc.perform(get(BASE_URL + '/adoptionPost/' + userId + "?page=0&size=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("\$.totalPages").value(1))
                .andExpect(jsonPath("\$.totalElements").value(1))
                .andExpect(jsonPath("\$.content[0].userId").value(userId))
                .andExpect(jsonPath("\$.content[0].adoptionPostId").value(adoptionPostId))
                .andExpect(jsonPath("\$.content[0].images").value(images))
                .andExpect(jsonPath("\$.content[0].title").value(title))

        where:
        userId   | images           | adoptionPostId | title
        'userId' | ['img1', 'img2'] | 'normalPostId' | 'title'
    }

    def "[저장피드 취득] 기부피드 취득"() {
        given:
        when(savedPostService.findDonationPostByUserId(userId, PageRequest.of(0, 5)))
                .thenReturn(new PageImpl<SimpleDonationPostResponse>(
                        List.of(
                                SimpleDonationPostResponse.builder()
                                        .donationPostId(donationPostId)
                                        .title(title)
                                        .images(images)
                                        .userId(userId)
                                        .build()
                        )
                ))

        expect:
        mvc.perform(get(BASE_URL + '/donationPost/' + userId + "?page=0&size=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("\$.totalPages").value(1))
                .andExpect(jsonPath("\$.totalElements").value(1))
                .andExpect(jsonPath("\$.content[0].userId").value(userId))
                .andExpect(jsonPath("\$.content[0].donationPostId").value(donationPostId))
                .andExpect(jsonPath("\$.content[0].images").value(images))
                .andExpect(jsonPath("\$.content[0].title").value(title))

        where:
        userId   | images           | donationPostId | title
        'userId' | ['img1', 'img2'] | 'normalPostId' | 'title'
    }
}
