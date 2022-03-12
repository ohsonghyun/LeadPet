package com.leadpet.www.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.leadpet.www.infrastructure.db.NormalPostsRepository
import com.leadpet.www.infrastructure.db.UsersRepository
import com.leadpet.www.infrastructure.domain.posts.NormalPosts
import com.leadpet.www.infrastructure.domain.users.LoginMethod
import com.leadpet.www.infrastructure.domain.users.Users
import com.leadpet.www.presentation.dto.request.AddNormalPostRequestDto
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
class PostControllerSpec extends Specification {
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
        usersRepository.save(
                Users.builder()
                        .loginMethod(LoginMethod.KAKAO)
                        .uid(uid)
                        .name('name')
                        .userType(Users.UserType.NORMAL)
                        .build())

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

    // TODO 일반 게시물 수정
    // TODO 일반 게시물 삭제

}
