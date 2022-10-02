package com.leadpet.www.presentation.dto.request.shelter;

import com.leadpet.www.infrastructure.domain.users.ShelterInfo;
import lombok.AccessLevel;

/**
 * UpdateShelterInfoRequestDto
 */
@lombok.Getter
@lombok.Builder
@lombok.NoArgsConstructor(access = AccessLevel.PROTECTED)
@lombok.AllArgsConstructor
public class UpdateShelterInfoRequestDto {
    private String shelterName;
    private String shelterAddress;
    private String shelterPhoneNumber;
    private String shelterManager;
    private String shelterHomePage;
    private String shelterIntro;
    private String shelterAccount;
    private String profileImage;
}
