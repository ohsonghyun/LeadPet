package com.leadpet.www.presentation.dto.response.user;

import io.swagger.annotations.ApiModel;
import lombok.AccessLevel;

/**
 * UserDetailResponseDto
 */

@ApiModel("유저 디테일 Response")
@lombok.Getter
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.Builder
public class UserDetailResponseDto {

    private String userId;
    private String email;
    /**
     * 총 댓글 수
     */
    // TODO
//    private long allReplyCount;
    /**
     * 총 기부 수
     */
    // TODO 기부는 어떻게? 어디서? 데이터가 없다.
//    private long allDonationCount;

}
