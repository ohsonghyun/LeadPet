package com.leadpet.www.presentation.dto.request.post.normal;

import io.swagger.annotations.ApiModel;

import javax.validation.constraints.NotNull;

@ApiModel("일반 게시물 삭제 Request")
@lombok.Getter
@lombok.Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class DeleteNormalPostRequestDto {
    @NotNull
    private String normalPostId;

    // 유저 판단 데이터
    @NotNull
    private String userId;
}
