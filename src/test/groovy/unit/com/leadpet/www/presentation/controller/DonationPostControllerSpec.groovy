package com.leadpet.www.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.leadpet.www.application.service.DonationPostService
import com.leadpet.www.infrastructure.domain.posts.DonationPosts
import com.leadpet.www.infrastructure.domain.users.Users
import com.leadpet.www.infrastructure.exception.login.UserNotFoundException
import com.leadpet.www.presentation.dto.request.post.donation.AddDonationPostRequestDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import spock.lang.Specification

import java.time.LocalDateTime

import static org.mockito.ArgumentMatchers.isA
import static org.mockito.Mockito.doThrow
import static org.mockito.Mockito.when
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * DonationPostControllerSpec
 */
@WebMvcTest(DonationPostController.class)
class DonationPostControllerSpec extends Specification {
    private static final String DONATION_POST_URL = "/v1/post/donation"

    @Autowired
    MockMvc mvc
    @MockBean
    DonationPostService donationPostService
    @Autowired
    ObjectMapper mapper

    def "[기부 피드 추가]: 에러: 존재 하지 않는 유저"() {
        given:
        doThrow(new UserNotFoundException())
                .when(donationPostService).addNewPost(isA(DonationPosts.class), isA(String.class))

        expect:
        mvc.perform(MockMvcRequestBuilders.post(DONATION_POST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper
                        .registerModule(new JavaTimeModule())
                        .writeValueAsString(
                                AddDonationPostRequestDto.builder()
                                        .startDate(startDate)
                                        .endDate(endDate)
                                        .title(title)
                                        .donationMethod(donationMethod)
                                        .contents(contents)
                                        .images(images)
                                        .userId(userId)
                                        .build())))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath('\$.error.detail').value('Error: 존재하지 않는 유저'))

        where:
        title   | contents   | images           | userId   | startDate           | endDate               | donationMethod
        'title' | 'contents' | ['img1', 'img2'] | 'uidkko' | LocalDateTime.now() | startDate.plusDays(5) | 'donationMethod'
    }

    def "[기부 피드 추가]: 정상"() {
        given:
        when(donationPostService.addNewPost(isA(DonationPosts.class), isA(String.class)))
                .thenReturn(
                        DonationPosts.builder()
                                .donationPostId('DP_dummy')
                                .startDate(startDate)
                                .endDate(endDate)
                                .title(title)
                                .donationMethod(donationMethod)
                                .contents(contents)
                                .images(images)
                                .user(Users.builder().userId(userId).build())
                                .build()
                )

        expect:
        mvc.perform(post(DONATION_POST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper
                        .registerModule(new JavaTimeModule())
                        .writeValueAsString(
                                AddDonationPostRequestDto.builder()
                                        .startDate(startDate)
                                        .endDate(endDate)
                                        .title(title)
                                        .donationMethod(donationMethod)
                                        .contents(contents)
                                        .images(images)
                                        .userId(userId)
                                        .build())))
                .andExpect(status().isOk())
                .andExpect(jsonPath('\$.donationPostId').value('DP_dummy'))
                .andExpect(jsonPath('\$.startDate').value(startDate.toString()))
                .andExpect(jsonPath('\$.endDate').value(endDate.toString()))
                .andExpect(jsonPath('\$.donationMethod').value(donationMethod))
                .andExpect(jsonPath('\$.title').value(title))
                .andExpect(jsonPath('\$.contents').value(contents))
                .andExpect(jsonPath('\$.images').value(images))
                .andExpect(jsonPath('\$.userId').value(userId))

        where:
        userId   | startDate           | endDate               | title        | donationMethod        | contents       | images
        'userId' | LocalDateTime.now() | startDate.plusDays(5) | 'dummyTitle' | 'dummyDonationMethod' | 'dummyContent' | ['img1', 'img2']
    }


}
