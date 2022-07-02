package com.leadpet.www.presentation.dto.response.shelter;

import com.leadpet.www.infrastructure.domain.users.AssessmentStatus;
import com.leadpet.www.infrastructure.domain.users.Users;
import io.swagger.annotations.ApiModel;
import lombok.AccessLevel;

/**
 * ShelterDetailDto
 */
@ApiModel("보호소 디테일 Response")
@lombok.Getter
@lombok.AllArgsConstructor(access = AccessLevel.PRIVATE)
@lombok.Builder
public class ShelterDetailDto {

    private String userId;
    private String shelterName;
    private AssessmentStatus assessmentStatus;
    private String shelterAddress;
    private String shelterPhoneNumber;
    // TODO 보호소 계좌번호도 가지고 있는지..
//    private String shelterAccount;
    private String shelterHomepage;

    /**
     * Users 객체로부터 DTO 생성
     *
     * @param shelter {@code Users}
     * @return {@code ShelterDetailDto}
     */
    public static ShelterDetailDto from(final Users shelter) {
        return ShelterDetailDto.builder()
                .userId(shelter.getUserId())
                .shelterName(shelter.getShelterName())
                .assessmentStatus(shelter.getShelterAssessmentStatus())
                .shelterAddress(shelter.getShelterAddress())
                .shelterPhoneNumber(shelter.getShelterPhoneNumber())
                .shelterHomepage(shelter.getShelterHomePage())
                .build();

    }
}
