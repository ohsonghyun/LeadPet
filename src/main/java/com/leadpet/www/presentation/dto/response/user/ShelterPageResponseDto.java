package com.leadpet.www.presentation.dto.response.user;

import com.leadpet.www.infrastructure.domain.users.AssessmentStatus;
import io.swagger.annotations.ApiModel;

/**
 * ShelterPageResponseDto
 */
@ApiModel("보호소 페이지네이션 Response")
@lombok.Getter
@lombok.Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class ShelterPageResponseDto {

    private String userId;
    /**
     * 보호소명
     */
    private String shelterName;
    /**
     * 총 게시글 수
     */
    private long allFeedCount;
    /**
     * 승인 요청 여부
     */
    private AssessmentStatus assessmentStatus;
}
