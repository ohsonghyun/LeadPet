package com.leadpet.www.presentation.dto.response.reply.normal;

import io.swagger.annotations.ApiModel;

import java.time.LocalDateTime;

/**
 * NormalReplyPageResponseDto
 */
@ApiModel("일상피드 댓글 페이지네이션 Response")
@lombok.Getter
@lombok.Builder
@lombok.RequiredArgsConstructor
public class NormalReplyPageResponseDto {
    private final String normalReplyId;
    private final String userId;
    private final String userName;
    private final String userProfileImage;
    private final String content;
    private final LocalDateTime createdDate;
}
