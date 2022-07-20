package com.leadpet.www.presentation.dto.request.liked;

import io.swagger.annotations.ApiModel;

/**
 * UpdateLikedRequestDto
 */
@ApiModel("좋아요 상태 갱신 Request")
@lombok.Getter
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.Builder
public class UpdateLikedRequestDto {
    private String userId;
    private String postId;
}
