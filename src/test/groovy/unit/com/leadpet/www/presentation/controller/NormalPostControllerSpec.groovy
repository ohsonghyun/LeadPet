package com.leadpet.www.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.leadpet.www.application.service.NormalPostService
import com.leadpet.www.infrastructure.db.posts.normalPost.condition.SearchNormalPostCondition
import com.leadpet.www.infrastructure.domain.posts.NormalPosts
import com.leadpet.www.infrastructure.domain.users.LoginMethod
import com.leadpet.www.infrastructure.domain.users.UserType
import com.leadpet.www.infrastructure.domain.users.Users
import com.leadpet.www.infrastructure.exception.PostNotFoundException
import com.leadpet.www.infrastructure.exception.UnauthorizedUserException
import com.leadpet.www.infrastructure.exception.login.UserNotFoundException
import com.leadpet.www.presentation.dto.request.post.AddNormalPostRequestDto
import com.leadpet.www.presentation.dto.request.post.UpdateNormalPostRequestDto
import com.leadpet.www.presentation.dto.request.post.normal.DeleteNormalPostRequestDto
import com.leadpet.www.presentation.dto.request.post.normal.SelectNormalPostRequestDto
import com.leadpet.www.presentation.dto.response.post.NormalPostResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.mockito.ArgumentMatchers.isA
import static org.mockito.Mockito.when
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * NormalPostControllerSpec
 */
@WebMvcTest(NormalPostController.class)
class NormalPostControllerSpec extends Specification {
    private static final String NORMAL_POST_URL = "/v1/post/normal"

    @Autowired
    MockMvc mvc
    @MockBean
    NormalPostService normalPostService

    def "[모든 일반 게시물 취득] Service로부터 받은 데이터를 DTO를 통해 반환한다"() {
        given:
        when(normalPostService.getNormalPostsWith(isA(SearchNormalPostCondition), isA(Pageable)))
                .thenReturn(
                        new PageImpl<NormalPostResponse>(
                                List.of(
                                        NormalPostResponse.builder()
                                                .normalPostId(postId)
                                                .title(title)
                                                .contents(contents)
                                                .userId(userId)
                                                .userName(userName)
                                                .likedCount(likedCount)
                                                .commentCount(commentCount)
                                                .liked(liked)
                                                .build()
                                )
                        ))

        expect:
        mvc.perform(get(NORMAL_POST_URL + '?page=0&size=5')
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath('\$.content.size()').value(1))
                .andExpect(jsonPath('\$.totalElements').value(1))
                .andExpect(jsonPath('\$.totalPages').value(1))
                .andExpect(jsonPath('\$.content[0].normalPostId').value(postId))
                .andExpect(jsonPath('\$.content[0].title').value(title))
                .andExpect(jsonPath('\$.content[0].contents').value(contents))
                .andExpect(jsonPath('\$.content[0].userId').value(userId))
                .andExpect(jsonPath('\$.content[0].userName').value(userName))
                .andExpect(jsonPath('\$.content[0].likedCount').value(likedCount))
                .andExpect(jsonPath('\$.content[0].commentCount').value(commentCount))
                .andExpect(jsonPath('\$.content[0].liked').value(liked))

        where:
        postId   | title   | contents   | userId   | userName   | likedCount | commentCount | liked
        'postId' | 'title' | 'contents' | 'userId' | 'userName' | 3L         | 3L           | true
    }

    def "[신규 일반 게시물 추가] Service로부터 받은 데이터를 DTO를 통해 반환한다"() {
        given:
        // dummy user
        Users user = Users.builder()
                .loginMethod(LoginMethod.KAKAO)
                .uid('dummyUid')
                .name('dummyName')
                .userId(userId)
                .userType(UserType.NORMAL)
                .build()
        // mock
        when(normalPostService.addNewPost(isA(NormalPosts.class), isA(String.class)))
                .thenReturn(
                        NormalPosts.builder()
                                .normalPostId("NP_a")
                                .title(title)
                                .contents(contents)
                                .images(images)
                                .user(user)
                                .build()
                )

        expect:
        mvc.perform(post(NORMAL_POST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(
                        AddNormalPostRequestDto.builder()
                                .title(title)
                                .contents(contents)
                                .images(images)
                                .userId(userId)
                                .build())))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.title').value(title))
                .andExpect(jsonPath('$.contents').value(contents))
                .andExpect(jsonPath('$.images').value(images))
                .andExpect(jsonPath('$.userId').isNotEmpty())

        where:
        title   | contents   | images           | userId
        'title' | 'contents' | ['img1', 'img2'] | 'uidkko'
    }

    def "일반 게시물 추가: 에러: 404 - 존재하지 않는 유저"() {
        given:
        when(normalPostService.addNewPost(isA(NormalPosts), isA(String)))
                .thenThrow(new UserNotFoundException('Error: 존재하지 않는 유저'))


        AddNormalPostRequestDto addNormalPostRequestDto = AddNormalPostRequestDto.builder()
                .title('title')
                .contents('contents')
                .userId('uidkko')
                .build()

        expect:
        mvc.perform(post(NORMAL_POST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(addNormalPostRequestDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath('\$.error.detail').value('Error: 존재하지 않는 유저'))
    }

    def "일반 게시물 수정: 정상"() {
        given:
        when(normalPostService.updateNormalPost(isA(NormalPosts.class), isA(String.class)))
                .thenReturn(NormalPosts.builder()
                        .normalPostId(postId)
                        .title(title)
                        .contents(contents)
                        .images(images)
                        .user(Users.builder()
                                .loginMethod(LoginMethod.KAKAO)
                                .uid('dummyUid')
                                .name('dummyName')
                                .userId(userId)
                                .userType(UserType.NORMAL)
                                .build())
                        .build())

        UpdateNormalPostRequestDto updateNormalPostRequestDto = UpdateNormalPostRequestDto.builder()
                .normalPostId(postId)
                .title(title)
                .contents(contents)
                .images(images)
                .userId(userId)
                .build()

        expect:
        mvc.perform(put(NORMAL_POST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updateNormalPostRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.normalPostId').value(postId))
                .andExpect(jsonPath('$.title').value(title))
                .andExpect(jsonPath('$.contents').value(contents))
                .andExpect(jsonPath('$.images').value(images))
                .andExpect(jsonPath('$.userId').value(userId))

        where:
        postId     | title   | contents   | images           | userId   | name   | uid
        'NP_dummy' | 'title' | 'contents' | ['img1', 'img2'] | 'userId' | 'name' | 'uid'
    }

    def "일반 게시물 수정: 404 - 게시물이 없을 때"() {
        given:
        when(normalPostService.updateNormalPost(isA(NormalPosts.class), isA(String.class)))
                .thenThrow(new PostNotFoundException())

        expect:
        mvc.perform(put(NORMAL_POST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(
                        UpdateNormalPostRequestDto.builder()
                                .normalPostId(normalPostId)
                                .title(updatedTitle)
                                .contents(updatedContents)
                                .images(updatedImages)
                                .userId('userId')
                                .build()
                )))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath('\$.error.detail').value('Error: 존재하지 않는 게시글'))

        where:
        normalPostId | updatedTitle   | updatedContents   | updatedImages
        'NP_a'       | 'updatedTitle' | 'updatedContents' | ['updated1', 'updated2']
    }

    def "일반 게시물 수정: 403 - 권한 없는 조작"() {
        given:
        when(normalPostService.updateNormalPost(isA(NormalPosts.class), isA(String.class)))
                .thenThrow(new UnauthorizedUserException())
        UpdateNormalPostRequestDto updateNormalPostRequestDto = UpdateNormalPostRequestDto.builder()
                .normalPostId(normalPostId)
                .title('updatedTitle')
                .contents('updatedContents')
                .images(['updated1', 'updated2'])
                .userId('userId')
                .build()

        expect:
        mvc.perform(put(NORMAL_POST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updateNormalPostRequestDto)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath('\$.error.detail').value(error))

        where:
        error             | normalPostId | loginMethod
        'Error: 권한 없는 조작' | 'NP_a'       | LoginMethod.KAKAO
    }

    def "일반 게시물 삭제: 정상"() {
        given:
        when(normalPostService.deleteNormalPost(isA(String.class), isA(String.class)))
                .thenReturn(postId)

        expect:
        mvc.perform(delete(NORMAL_POST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(
                        DeleteNormalPostRequestDto.builder()
                                .normalPostId(postId)
                                .userId(userId)
                                .build()
                )))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.normalPostId').value(postId))

        where:
        postId   | userId
        'postId' | 'userId'
    }

    def "일반 게시물 삭제: 에러: 404-존재하지 않는 게시글"() {
        given:
        when(normalPostService.deleteNormalPost(isA(String.class), isA(String.class)))
                .thenThrow(new PostNotFoundException())

        expect:
        mvc.perform(delete(NORMAL_POST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(
                        DeleteNormalPostRequestDto.builder()
                                .normalPostId('postId')
                                .userId('userId')
                                .build()
                )))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath('\$.error.detail').value('Error: 존재하지 않는 게시글'))
    }

    def "일반 게시물 삭제: 에러: 403-권한 없는 조작"() {
        given:
        when(normalPostService.deleteNormalPost(isA(String.class), isA(String.class)))
                .thenThrow(new UnauthorizedUserException())

        expect:
        mvc.perform(delete(NORMAL_POST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(
                        DeleteNormalPostRequestDto.builder()
                                .normalPostId('postId')
                                .userId('userId')
                                .build()
                )))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath('\$.error.detail').value('Error: 권한 없는 조작'))
    }

    def "일반 게시물 상세조회: 정상"() {
        given:
        // dummy user
        Users user = Users.builder()
                .loginMethod(LoginMethod.KAKAO)
                .uid('dummyUid')
                .name('dummyName')
                .userId('uidkko')
                .userType(UserType.NORMAL)
                .build()

        when(normalPostService.selectNormalPost(isA(String.class)))
                .thenReturn(NormalPosts.builder()
                        .normalPostId(normalPostId)
                        .title(title)
                        .contents(contents)
                        .images(images)
                        .user(user)
                        .build()
                )

        expect:
        mvc.perform(get(NORMAL_POST_URL + '/' + normalPostId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath('\$.normalPostId').value(normalPostId))
                .andExpect(jsonPath('\$.title').value(title))
                .andExpect(jsonPath('\$.contents').value(contents))
                .andExpect(jsonPath('\$.images').value(images))

        where:
        normalPostId | title   | contents   | images
        'NP_app00'   | 'title' | 'contents' | ['image1', 'image2']
    }
}
