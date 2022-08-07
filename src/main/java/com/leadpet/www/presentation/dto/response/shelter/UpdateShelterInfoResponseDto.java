package com.leadpet.www.presentation.dto.response.shelter;

import com.leadpet.www.infrastructure.domain.users.Users;
import io.swagger.annotations.ApiModel;
import lombok.AccessLevel;

/**
 * UpdateShelterInfoResponseDto
 */
@ApiModel("보호소 정보 수정 Response")
@lombok.Getter
@lombok.AllArgsConstructor(access = AccessLevel.PRIVATE)
@lombok.Builder
public class UpdateShelterInfoResponseDto {

    private String userId;
    private String shelterName;
    private String shelterAddress;
    private String shelterPhoneNumber;
    private String shelterAccount;
    private String shelterHomepage;
    private String shelterManager;
    private String shelterIntro;


    /**
     * Users 객체로부터 DTO 생성
     *
     * @param shelter {@code Users}
     * @return {@code UpdateShelterInfoResponseDto}
     */
    public static UpdateShelterInfoResponseDto from(final Users shelter) {
        return UpdateShelterInfoResponseDto.builder()
                .userId(shelter.getUserId())
                .shelterName(shelter.getShelterName())
                .shelterAddress(shelter.getShelterAddress())
                .shelterPhoneNumber(shelter.getShelterPhoneNumber())
                .shelterHomepage(shelter.getShelterHomePage())
                .shelterManager(shelter.getShelterManager())
                .shelterIntro(shelter.getShelterIntro())
                .shelterAccount(shelter.getShelterAccount())
                .build();
    }
}
