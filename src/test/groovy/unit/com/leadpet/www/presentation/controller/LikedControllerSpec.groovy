package com.leadpet.www.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.leadpet.www.application.service.liked.LikedService
import com.leadpet.www.infrastructure.domain.liked.LikedResult
import com.leadpet.www.presentation.dto.request.liked.UpdateLikedRequestDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.mockito.ArgumentMatchers.isA
import static org.mockito.Mockito.when
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * LikedControllerSpec
 */
@WebMvcTest(LikedController)
class LikedControllerSpec extends Specification {
    private static final String LIKED_URL = "/v1/liked"

    @Autowired
    MockMvc mvc

    @Autowired
    ObjectMapper mapper

    @MockBean
    LikedService likedService

    def "유저/피드별 좋아요 상태를 갱신"() {
        given:
        when(likedService.saveOrDelete(isA(String.class), isA(String.class))).thenReturn(mockStatus)

        expect:
        mvc.perform(post(LIKED_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper
                        .writeValueAsString(
                                UpdateLikedRequestDto.builder()
                                        .userId('userId')
                                        .postId('postId')
                                        .build())))
                .andExpect(expectedResponse)

        where:
        mockStatus          | expectedResponse
        LikedResult.CREATED | status().isCreated()
        LikedResult.DELETED | status().isOk()
    }
}
