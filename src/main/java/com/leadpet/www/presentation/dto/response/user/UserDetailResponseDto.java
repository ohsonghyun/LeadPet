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
    private String userName;
    private String email;

    /**
     * 총 댓글 수
     */
    private Long allReplyCount;
}
