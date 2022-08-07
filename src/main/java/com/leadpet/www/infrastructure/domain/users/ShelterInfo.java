package com.leadpet.www.infrastructure.domain.users;

import lombok.AccessLevel;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * ShelterInfo
 */
@Embeddable
@lombok.Getter
@lombok.Builder
@lombok.NoArgsConstructor(access = AccessLevel.PROTECTED)
@lombok.AllArgsConstructor
public class ShelterInfo {
    private String shelterName;
    private String shelterAddress;
    private String shelterPhoneNumber;
    private String shelterManager;
    private String shelterHomePage;
    private String shelterIntro;
    private String shelterAccount;

    // TODO 이거 내부적으로 설정하도록 해야함.
    @Column(name = "shelter_assessment_status")
    @Enumerated(value = EnumType.STRING)
    private AssessmentStatus shelterAssessmentStatus;
}
