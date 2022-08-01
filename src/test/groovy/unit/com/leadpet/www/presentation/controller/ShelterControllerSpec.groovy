package com.leadpet.www.presentation.controller

import com.leadpet.www.application.service.UserService
import com.leadpet.www.infrastructure.db.users.condition.SearchShelterCondition
import com.leadpet.www.infrastructure.domain.users.AssessmentStatus
import com.leadpet.www.infrastructure.domain.users.LoginMethod
import com.leadpet.www.infrastructure.domain.users.UserType
import com.leadpet.www.infrastructure.domain.users.Users
import com.leadpet.www.infrastructure.exception.login.UserNotFoundException
import com.leadpet.www.presentation.dto.response.user.ShelterPageResponseDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.mockito.ArgumentMatchers.isA
import static org.mockito.Mockito.when
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
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
                        List.of(new ShelterPageResponseDto(userId, shetlerName, 1, assessmentStatus, shelterAddress, shelterPhoneNumber, shelterHomePage, profileImage))
                ))

        expect:
        mvc.perform(get(SHELTER_URL + "/list")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath('\$.content[0].userId').value(userId))
                .andExpect(jsonPath('\$.content[0].shelterName').value(shetlerName))
                .andExpect(jsonPath('\$.content[0].assessmentStatus').value(assessmentStatus.name()))
                .andExpect(jsonPath('\$.content[0].shelterAddress').value(shelterAddress))
                .andExpect(jsonPath('\$.content[0].shelterPhoneNumber').value(shelterPhoneNumber))
                .andExpect(jsonPath('\$.content[0].shelterHomePage').value(shelterHomePage))
                .andExpect(jsonPath('\$.content[0].profileImage').value(profileImage))

        where:
        userId   | shetlerName   | assessmentStatus           | profileImage   | shelterAddress     | shelterPhoneNumber | shelterHomePage
        'userId' | 'shelterName' | AssessmentStatus.COMPLETED | 'profileImage' | '헬로우 월드 주소 123-12' | '010-1234-1234'    | 'www.thor.com'
    }

    def "[보호소 디테일 취득] 정상"() {
        given:
        when(userService.shelterDetail(isA(String.class)))
                .thenReturn(
                        Users.builder()
                                .userId(userId)
                                .loginMethod(loginMethod)
                                .uid(uid)
                                .name(name)
                                .userType(userType)
                                .shelterName(shelterName)
                                .shelterAddress(shelterAddress)
                                .shelterAssessmentStatus(shelterAssessmentStatus)
                                .shelterPhoneNumber(shelterPhoneNumber)
                                .shelterHomePage(shelterHomePage)
                                .shelterManager(shelterManager)
                                .profileImage(profileImage)
                                .shelterIntro(shelterIntro)
                                .shelterAccount(shelterAccount)
                                .build())

        expect:
        mvc.perform(get(SHELTER_URL + '/' + userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath('\$.userId').value(userId))
                .andExpect(jsonPath('\$.shelterName').value(shelterName))
                .andExpect(jsonPath('\$.assessmentStatus').value(shelterAssessmentStatus.name()))
                .andExpect(jsonPath('\$.shelterAddress').value(shelterAddress))
                .andExpect(jsonPath('\$.shelterPhoneNumber').value(shelterPhoneNumber))
                .andExpect(jsonPath('\$.shelterHomepage').value(shelterHomePage))
                .andExpect(jsonPath('\$.shelterManager').value(shelterManager))
                .andExpect(jsonPath('\$.profileImage').value(profileImage))
                .andExpect(jsonPath('\$.shelterIntro').value(shelterIntro))
                .andExpect(jsonPath('\$.shelterAccount').value(shelterAccount))

        where:
        userId   | loginMethod       | uid   | name   | userType         | shelterName | shelterAddress                 | shelterAssessmentStatus  | shelterManager   | profileImage   | shelterIntro   | shelterAccount   | shelterPhoneNumber | shelterHomePage
        'userId' | LoginMethod.APPLE | 'uid' | 'name' | UserType.SHELTER | '토르 보호소'    | '서울특별시 헬로우 월드 주소 어디서나 123-123' | AssessmentStatus.PENDING | 'shelterManager' | 'profileImage' | 'shelterIntro' | 'shelterAccount' | "010-1234-1234"    | "www.thor.com"
    }

    def "[보호소 디테일 취득] 에러: userId가 null"() {
        given:
        when(userService.shelterDetail(isA(String.class)))
                .thenThrow(new UserNotFoundException())

        expect:
        mvc.perform(get(SHELTER_URL + '/' + userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(responseStatus)

        where:
        testCase             | userId     | responseStatus
        'userId가 null인 경우'   | ''         | status().isNotFound()
        '존재하지 않는 userId인 경우' | 'notExist' | status().isNotFound()
    }

}
