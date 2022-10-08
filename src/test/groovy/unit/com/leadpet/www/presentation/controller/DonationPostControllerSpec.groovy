package com.leadpet.www.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.leadpet.www.application.service.DonationPostService
import com.leadpet.www.infrastructure.db.posts.donationPost.condition.SearchDonationPostCondition
import com.leadpet.www.infrastructure.domain.donation.DonationMethod
import com.leadpet.www.infrastructure.domain.pet.AnimalType
import com.leadpet.www.infrastructure.domain.pet.Gender
import com.leadpet.www.infrastructure.domain.pet.Neutering
import com.leadpet.www.infrastructure.domain.posts.AdoptionPosts
import com.leadpet.www.infrastructure.domain.posts.DonationPosts
import com.leadpet.www.infrastructure.domain.users.Users
import com.leadpet.www.infrastructure.exception.login.UserNotFoundException
import com.leadpet.www.presentation.dto.request.post.donation.AddDonationPostRequestDto
import com.leadpet.www.presentation.dto.response.post.donation.DonationPostPageResponseDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import spock.lang.Specification

import java.time.LocalDateTime

import static org.mockito.ArgumentMatchers.isA
import static org.mockito.Mockito.doThrow
import static org.mockito.Mockito.when
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
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
        'title' | 'contents' | ['img1', 'img2'] | 'uidkko' | LocalDateTime.now() | startDate.plusDays(5) | DonationMethod.ACCOUNT
    }

    def "[기부 피드 추가]: 정상"() {
        given:
        final startDate = LocalDateTime.now()
        final endDate = startDate.plusDays(5)
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
                .andExpect(jsonPath('\$.startDate').isNotEmpty())
                .andExpect(jsonPath('\$.endDate').isNotEmpty())
                .andExpect(jsonPath('\$.donationMethod').value(donationMethod.name()))
                .andExpect(jsonPath('\$.title').value(title))
                .andExpect(jsonPath('\$.contents').value(contents))
                .andExpect(jsonPath('\$.images').value(images))
                .andExpect(jsonPath('\$.userId').value(userId))

        where:
        userId   | title        | donationMethod         | contents       | images
        'userId' | 'dummyTitle' | DonationMethod.ACCOUNT | 'dummyContent' | ['img1', 'img2']
    }

    def "[기부 피드 목록 취득]: 정상"() {
        when(donationPostService.searchAll(isA(SearchDonationPostCondition), isA(Pageable)))
                .thenReturn(new PageImpl<DonationPostPageResponseDto>(
                        List.of(
                                DonationPostPageResponseDto.builder()
                                        .donationPostId('postId')
                                        .startDate(startDate)
                                        .endDate(endDate)
                                        .title('title')
                                        .donationMethod(DonationMethod.ACCOUNT)
                                        .contents('contents')
                                        .images(['img1', 'img2'])
                                        .userId('userId')
                                        .userName('userName')
                                        .build()
                        )
                ))

        expect:
        mvc.perform(get(DONATION_POST_URL + '?page=0&size=5')
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath('\$.content.size()').value(1))
                .andExpect(jsonPath('\$.totalElements').value(1))
                .andExpect(jsonPath('\$.totalPages').value(1))

        where:
        startDate           | endDate
        LocalDateTime.now() | LocalDateTime.now().plusDays(10)
    }

}
