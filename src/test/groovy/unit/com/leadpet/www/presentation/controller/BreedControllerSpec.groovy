package com.leadpet.www.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.leadpet.www.application.service.breed.BreedService
import com.leadpet.www.infrastructure.domain.breed.Breed
import com.leadpet.www.infrastructure.domain.pet.AnimalType
import com.leadpet.www.presentation.dto.request.breed.AddBreedRequest
import com.leadpet.www.presentation.dto.response.breed.SearchBreedResponse
import com.leadpet.www.presentation.dto.response.breed.SearchBreedResultResponse
import org.assertj.core.api.Assertions
import org.hamcrest.Matchers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.mockito.ArgumentMatchers.isA
import static org.mockito.Mockito.when
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * BreedControllerSpec
 */
@WebMvcTest(BreedController)
class BreedControllerSpec extends Specification {
    private final static String BASE_URL = '/v1/breed'

    @Autowired
    MockMvc mvc

    @Autowired
    ObjectMapper objectMapper

    @MockBean
    private BreedService breedService

    def "품종 정보를 추가"() {
        given:
        when(breedService.save(isA(String.class), isA(String.class)))
                .thenReturn(
                        Breed.builder()
                                .breedId(breedId)
                                .category(category)
                                .breedName(breedName)
                                .build()
                )

        expect:
        mvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        objectMapper.writeValueAsString(
                                AddBreedRequest.builder()
                                        .category(category)
                                        .breedName(breedName)
                                        .build())))
                .andExpect(status().isOk())
                .andExpect(jsonPath('\$.breedId').isNotEmpty())
                .andExpect(jsonPath('\$.category').value('가'))
                .andExpect(jsonPath('\$.breedName').value('골든 리트리버'))

        where:
        breedId   | category | breedName
        'breedId' | '가'      | '골든 리트리버'
    }

    def "품종 리스트를 취득"() {
        given:
        when(breedService.findGroupByCategory()).thenReturn(
                SearchBreedResultResponse.builder()
                        .results(List.of(
                                new SearchBreedResultResponse.Result(
                                        '가',
                                        List.of(
                                                new SearchBreedResponse('골든 리트리버', AnimalType.DOG),
                                                new SearchBreedResponse('고든 셰터', AnimalType.DOG)
                                        )),
                                new SearchBreedResultResponse.Result(
                                        '사',
                                        List.of(
                                                new SearchBreedResponse('시츄', AnimalType.DOG),
                                                new SearchBreedResponse('시바', AnimalType.DOG),
                                                new SearchBreedResponse('스피츠', AnimalType.DOG),
                                        ))
                        ))
                        .build()
        )

        expect:
        mvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath('\$.results.size()').value(2))
                .andExpect(jsonPath('\$.results[0].index').value('가'))
                .andExpect(jsonPath('\$.results[0].breedList.size()').value(2))
                .andExpect(jsonPath('\$.results[0].breedList[0].breedName').value('골든 리트리버'))
                .andExpect(jsonPath('\$.results[0].breedList[0].animalType').value('DOG'))
                .andExpect(jsonPath('\$.results[0].breedList[1].breedName').value('고든 셰터'))
                .andExpect(jsonPath('\$.results[0].breedList[1].animalType').value('DOG'))
                .andExpect(jsonPath('\$.results[1].index').value('사'))
                .andExpect(jsonPath('\$.results[1].breedList.size()').value(3))
                .andExpect(jsonPath('\$.results[1].breedList[0].breedName').value('시츄'))
                .andExpect(jsonPath('\$.results[1].breedList[0].animalType').value('DOG'))
                .andExpect(jsonPath('\$.results[1].breedList[1].breedName').value('시바'))
                .andExpect(jsonPath('\$.results[1].breedList[1].animalType').value('DOG'))
                .andExpect(jsonPath('\$.results[1].breedList[2].breedName').value('스피츠'))
                .andExpect(jsonPath('\$.results[1].breedList[2].animalType').value('DOG'))
    }

    def "품종 전체 카운트 반환"() {
        given:
        when(breedService.count()).thenReturn(count)

        expect:
        mvc.perform(get(BASE_URL + '/count'))
                .andExpect(status().isOk())
                .andExpect(jsonPath("\$.count").value(count))
        where:
        count << [0L, 10L, 100L]
    }
}
