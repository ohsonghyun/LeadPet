package com.leadpet.www.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.leadpet.www.application.service.breed.BreedService
import com.leadpet.www.infrastructure.domain.breed.Breed
import com.leadpet.www.presentation.dto.request.breed.AddBreedRequest
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
                [
                        '가': group1,
                        '사': group2
                ]
        )

        expect:
        mvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath('\$.가.size()').value(2))
                .andExpect(jsonPath('\$.가').value(group1))
                .andExpect(jsonPath('\$.사.size()').value(3))
                .andExpect(jsonPath('\$.사').value(group2))

        where:
        group1               | group2
        ['골든 리트리버', '고든 셰터'] | ['시츄', '시바', '스피츠']
    }
}
