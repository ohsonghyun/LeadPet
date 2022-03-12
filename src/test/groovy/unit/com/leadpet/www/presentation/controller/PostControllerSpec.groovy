package com.leadpet.www.presentation.controller

import com.leadpet.www.application.service.NormalPostService
import com.leadpet.www.infrastructure.db.NormalPostsRepository
import com.leadpet.www.infrastructure.domain.posts.NormalPosts
import org.hamcrest.Matchers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
class PostControllerSpec extends Specification {
    private final String POST_URL = "/v1/post"

    @Autowired
    WebApplicationContext webApplicationContext
    @Autowired
    NormalPostService normalPostService
    @Autowired
    NormalPostsRepository normalPostsRepository
    MockMvc mvc

    def setup() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build()
    }

    def cleanup() {
        normalPostsRepository.deleteAll()
    }

    def "모든 일반게시물 데이터를 취득한다"() {
        given:
        normalPostsRepository.saveAll(List.of(
                NormalPosts.builder().title("title").contents("contents").build(),
                NormalPosts.builder().title("title").contents("contents").build(),
                NormalPosts.builder().title("title").contents("contents").build()
        ))

        expect:
        mvc.perform(get(POST_URL + "/allNormal"))
            .andExpect(status().isOk())
            .andExpect(jsonPath('$', Matchers.hasSize(3)))
    }

}
