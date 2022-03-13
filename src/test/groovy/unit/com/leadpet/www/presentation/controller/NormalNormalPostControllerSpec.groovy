package com.leadpet.www.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.leadpet.www.infrastructure.db.NormalPostsRepository
import com.leadpet.www.infrastructure.db.UsersRepository
import com.leadpet.www.infrastructure.domain.posts.NormalPosts
import com.leadpet.www.infrastructure.domain.users.LoginMethod
import com.leadpet.www.infrastructure.domain.users.UserType
import com.leadpet.www.infrastructure.domain.users.Users
import com.leadpet.www.presentation.dto.request.post.AddNormalPostRequestDto
import com.leadpet.www.presentation.dto.request.post.UpdateNormalPostRequestDto
import org.hamcrest.Matchers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * NormalPostControllerSpec
 */
@SpringBootTest
class NormalNormalPostControllerSpec extends Specification {
    private final String POST_URL = "/v1/post"

    @Autowired
    WebApplicationContext webApplicationContext
    @Autowired
    UsersRepository usersRepository
    @Autowired
    NormalPostsRepository normalPostsRepository
    MockMvc mvc

    def setup() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build()
    }

    def cleanup() {
        usersRepository.deleteAll()
        normalPostsRepository.deleteAll()
    }

    def "모든 일반게시물 데이터를 취득한다"() {
        given:
        normalPostsRepository.saveAll(List.of(
                NormalPosts.builder().title("title").contents("contents").userId(1).build(),
                NormalPosts.builder().title("title").contents("contents").userId(1).build(),
                NormalPosts.builder().title("title").contents("contents").userId(1).build()
        ))

        expect:
        mvc.perform(get(POST_URL + "/allNormal"))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$', Matchers.hasSize(3)))
    }

    def "일반 게시물 추가: 정상"() {
        given:
        addNewUser(LoginMethod.KAKAO, uid, 'name', UserType.NORMAL)
        AddNormalPostRequestDto addNormalPostRequestDto = AddNormalPostRequestDto.builder()
                .title(title)
                .contents(contents)
                .images(images)
                .tags(tags)
                .loginMethod(LoginMethod.KAKAO)
                .uid(uid)
                .build()

        expect:
        mvc.perform(post(POST_URL + '/addNormal')
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(addNormalPostRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.title').value(title))
                .andExpect(jsonPath('$.contents').value(contents))
                .andExpect(jsonPath('$.images').value(images))
                .andExpect(jsonPath('$.tags').value(tags))
                .andExpect(jsonPath('$.userId').isNotEmpty())

        where:
        title   | contents   | images           | tags                     | uid
        'title' | 'contents' | ['img1', 'img2'] | ['tag1', 'tag2', 'tag3'] | 'uid'
    }

    def "일반 게시물 추가: 에러: 404 - 존재하지 않는 유저"() {
        given:
        AddNormalPostRequestDto addNormalPostRequestDto = AddNormalPostRequestDto.builder()
                .title('title')
                .contents('contents')
                .loginMethod(LoginMethod.KAKAO)
                .uid('uid')
                .build()

        expect:
        mvc.perform(post(POST_URL + '/addNormal')
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(addNormalPostRequestDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath('\$.error.detail').value('Error: 존재하지 않는 유저'))
    }

    def "일반 게시물 수정: 정상"() {
        given:
        // 유저 생성
        Users user = addNewUser(loginMethod, uid, 'name', UserType.NORMAL)
        Long userId = user.getUserId()

        // 기존 일반 게시글 생성
        NormalPosts existingPost = normalPostsRepository.save(NormalPosts.builder().title("title").contents("contents").userId(userId).build())
        Long normalPostId = existingPost.getNormalPostId()

        UpdateNormalPostRequestDto updateNormalPostRequestDto = UpdateNormalPostRequestDto.builder()
                .normalPostId(normalPostId)
                .title(updatedTitle)
                .contents(updatedContents)
                .images(updatedImages)
                .tags(updatedTags)
                .loginMethod(loginMethod)
                .uid(uid)
                .build()

        expect:
        mvc.perform(put(POST_URL + '/updateNormal')
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updateNormalPostRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.normalPostId').value(normalPostId))
                .andExpect(jsonPath('$.title').value(updatedTitle))
                .andExpect(jsonPath('$.contents').value(updatedContents))
                .andExpect(jsonPath('$.images').value(updatedImages))
                .andExpect(jsonPath('$.tags').value(updatedTags))
                .andExpect(jsonPath('$.uid').value(uid))
                .andExpect(jsonPath('$.loginMethod').value(loginMethod.name()))

        NormalPosts updatedPost = normalPostsRepository.findByNormalPostIdAndUserId(normalPostId, userId)
        updatedPost != null
        updatedPost.title == updatedTitle
        updatedPost.contents == updatedContents
        updatedPost.images == updatedImages
        updatedPost.tags == updatedTags

        where:
        testCase | uid   | loginMethod       | updatedTitle   | updatedContents   | updatedImages            | updatedTags
        '정상'     | 'uid' | LoginMethod.KAKAO | 'updatedTitle' | 'updatedContents' | ['updated1', 'updated2'] | ['updated1', 'updated2']
    }
    // TODO 일반 게시물 수정: 에러: 404 존재하지 않는 게시글
    // TODO 일반 게시물 수정: 에러: 401 권한이 없는 유저

    // TODO 일반 게시물 삭제

    // ----------------------------------------------------

    /**
     * Users 등록
     */
    private Users addNewUser(LoginMethod loginMethod, String uid, String name, UserType userType) {
        return usersRepository.save(
                Users.builder()
                        .loginMethod(loginMethod)
                        .uid(uid)
                        .name(name)
                        .userType(userType)
                        .build())
    }

}
