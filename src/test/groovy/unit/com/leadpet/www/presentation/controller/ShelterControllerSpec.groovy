package com.leadpet.www.presentation.controller

import com.leadpet.www.application.service.UserService
import com.leadpet.www.infrastructure.db.users.condition.SearchShelterCondition
import com.leadpet.www.infrastructure.domain.users.AssessmentStatus
import com.leadpet.www.presentation.dto.response.user.ShelterPageResponseDto
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageImpl
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification

import java.awt.print.Pageable

import static org.mockito.ArgumentMatchers.isA
import static org.mockito.Mockito.when
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(ShelterController.class)
class ShelterControllerSpec extends Specification {
    private final String SHELTER_URL = "/v1/shelter"

    @Autowired
    MockMvc mvc
    @MockBean
    UserService userService

    def "[보호소 목록 취득] 정상"() {
        given:
        when(userService.searchShelters(isA(SearchShelterCondition.class), isA(Pageable.class)))
                .thenReturn(new PageImpl<ShelterPageResponseDto>(
                        List.of(new ShelterPageResponseDto("shelterName", 1, AssessmentStatus.COMPLETED))
                ))

        expect:
        mvc.perform(get(SHELTER_URL + "/list")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
    }


}
