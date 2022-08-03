package com.leadpet.www.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.leadpet.www.application.service.AdoptionPostService
import com.leadpet.www.infrastructure.db.adoptionPost.condition.SearchAdoptionPostCondition
import com.leadpet.www.infrastructure.domain.donation.DonationMethod
import com.leadpet.www.infrastructure.domain.pet.AnimalType
import com.leadpet.www.infrastructure.domain.pet.Gender
import com.leadpet.www.infrastructure.domain.pet.Neutering
import com.leadpet.www.infrastructure.domain.posts.AdoptionPosts
import com.leadpet.www.infrastructure.domain.users.Users
import com.leadpet.www.infrastructure.exception.login.UserNotFoundException
import com.leadpet.www.presentation.dto.request.post.adoption.AddAdoptionPostRequestDto
import com.leadpet.www.presentation.dto.request.post.donation.AddDonationPostRequestDto
import com.leadpet.www.presentation.dto.response.post.adoption.AdoptionPostPageResponseDto
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
 * AdoptionPostControllerSpec
 */
@WebMvcTest(AdoptionPostController.class)
class AdoptionPostControllerSpec extends Specification {
    private static final String ADOPTION_POST_URL = "/v1/post/adoption"

    @Autowired
    MockMvc mvc
    @MockBean
    AdoptionPostService adoptionPostService
    @Autowired
    ObjectMapper mapper

    def "[입양 피드 추가]: 에러: 존재 하지 않는 유저"() {
        given:
        doThrow(new UserNotFoundException())
                .when(adoptionPostService).addNewPost(isA(AdoptionPosts.class), isA(String.class))

        expect:
        mvc.perform(MockMvcRequestBuilders.post(ADOPTION_POST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper
                        .registerModule(new JavaTimeModule())
                        .writeValueAsString(
                                AddAdoptionPostRequestDto.builder()
                                        .startDate(startDate)
                                        .endDate(endDate)
                                        .title(title)
                                        .contents(contents)
                                        .images(images)
                                        .userId(userId)
                                        .build())))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath('\$.error.detail').value('Error: 존재하지 않는 유저'))

        where:
        title   | contents   | images           | userId   | startDate           | endDate
        'title' | 'contents' | ['img1', 'img2'] | 'uidkko' | LocalDateTime.now() | startDate.plusDays(5)
    }

    def "[입양 피드 추가]: 정상"() {
        given:
        final startDate = LocalDateTime.now()
        final endDate = startDate.plusDays(5)
        final euthanasiaDate = endDate.plusDays(5).withNano(0)

        when(adoptionPostService.addNewPost(isA(AdoptionPosts.class), isA(String.class)))
                .thenReturn(
                        AdoptionPosts.builder()
                                .adoptionPostId(postId)
                                .startDate(startDate)
                                .endDate(endDate)
                                .euthanasiaDate(euthanasiaDate)
                                .title(title)
                                .contents(contents)
                                .animalType(animalType)
                                .species(species)
                                .gender(gender)
                                .neutering(neutering)
                                .age(age)
                                .disease(disease)
                                .images(images)
                                .user(Users.builder().userId(userId).build())
                                .build()
                )

        expect:
        mvc.perform(post(ADOPTION_POST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper
                        .registerModule(new JavaTimeModule())
                        .writeValueAsString(
                                AddAdoptionPostRequestDto.builder()
                                        .startDate(startDate)
                                        .endDate(endDate)
                                        .euthanasiaDate(euthanasiaDate)
                                        .title(title)
                                        .contents(contents)
                                        .animalType(animalType)
                                        .species(species)
                                        .gender(gender)
                                        .neutering(neutering)
                                        .age(age)
                                        .disease(disease)
                                        .images(images)
                                        .userId(userId)
                                        .build())))
                .andExpect(status().isOk())
                .andExpect(jsonPath('\$.adoptionPostId').value(postId))
                .andExpect(jsonPath('\$.startDate').isNotEmpty())
                .andExpect(jsonPath('\$.endDate').isNotEmpty())
                .andExpect(jsonPath('\$.euthanasiaDate').isNotEmpty())
                .andExpect(jsonPath('\$.title').value(title))
                .andExpect(jsonPath('\$.contents').value(contents))
                .andExpect(jsonPath('\$.animalType').value(animalType.name()))
                .andExpect(jsonPath('\$.gender').value(gender.name()))
                .andExpect(jsonPath('\$.neutering').value(neutering.name()))
                .andExpect(jsonPath('\$.age').value(age))
                .andExpect(jsonPath('\$.disease').value(disease))
                .andExpect(jsonPath('\$.images').value(images))
                .andExpect(jsonPath('\$.userId').value(userId))

        where:
        postId   | userId   | title   | contents   | animalType     | species   | gender      | neutering     | age | disease     | images
        'postId' | 'userId' | 'title' | 'contents' | AnimalType.DOG | 'species' | Gender.MALE | Neutering.YES |  1  | 'disease'   | ['img1', 'img2']
    }

    def "[입양 피드 목록 취득]: 정상"() {
        given:
        when(adoptionPostService.searchAll(isA(SearchAdoptionPostCondition.class), isA(Pageable.class)))
                .thenReturn(new PageImpl<AdoptionPostPageResponseDto>(
                        List.of(
                                AdoptionPostPageResponseDto.builder()
                                        .adoptionPostId('postId')
                                        .startDate(startDate)
                                        .endDate(endDate)
                                        .euthanasiaDate(endDate)
                                        .title('title')
                                        .contents('contents')
                                        .animalType(AnimalType.DOG)
                                        .species('species')
                                        .gender(Gender.MALE)
                                        .neutering(Neutering.YES)
                                        .age(1)
                                        .disease('disease')
                                        .images(['img1', 'img2'])
                                        .userId('userId')
                                        .build()
                        )
                ))

        expect:
        mvc.perform(get(ADOPTION_POST_URL + '?page=0&size=5')
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
