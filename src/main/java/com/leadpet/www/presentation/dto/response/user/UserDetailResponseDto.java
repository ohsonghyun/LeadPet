package com.leadpet.www.presentation.dto.response.user;

import io.swagger.annotations.ApiModel;
import lombok.AccessLevel;

/**
 * UserDetailResponseDto
 */

@ApiModel("유저 디테일 Response")
@lombok.Getter
@lombok.AllArgsConstructor(access = AccessLevel.PRIVATE)
@lombok.Builder
public class UserDetailResponseDto {

    private String userId;
    private String email;
    /**
     * 총 댓글 수
     */
    private long allReplyCount;
    /**
     * 총 기부 수
     */
    private long allDonationCount;



}
