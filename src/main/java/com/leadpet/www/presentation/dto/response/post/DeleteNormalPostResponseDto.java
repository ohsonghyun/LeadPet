package com.leadpet.www.presentation.dto.response.post;

import io.swagger.annotations.ApiModel;
import lombok.AccessLevel;
import org.springframework.lang.NonNull;

/**
 * DeleteNormalPostResponseDto
 */
@ApiModel("일반 게시물 삭제 Response")
@lombok.Getter
@lombok.Builder(access = AccessLevel.PRIVATE)
public class DeleteNormalPostResponseDto {
    private String normalPostId;

    public static DeleteNormalPostResponseDto from(@NonNull final String normalPostId) {
        return DeleteNormalPostResponseDto.builder()
                .normalPostId(normalPostId)
                .build();
    }
}
