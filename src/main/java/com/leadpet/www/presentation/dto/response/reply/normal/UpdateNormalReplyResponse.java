package com.leadpet.www.presentation.dto.response.reply.normal;

import com.leadpet.www.infrastructure.domain.reply.normal.NormalReply;
import io.swagger.annotations.ApiModel;

import java.time.LocalDateTime;

/**
 * UpdateNormalReplyResponse
 */
@ApiModel("일상피드 댓글 수정 Response")
@lombok.Getter
@lombok.Builder(access = lombok.AccessLevel.PRIVATE)
public class UpdateNormalReplyResponse {
    private String normalReplyId;
    private String userId;
    private String userName;
    private String content;
    private LocalDateTime createdDate;

    public static UpdateNormalReplyResponse from(final NormalReply reply) {
        return UpdateNormalReplyResponse.builder()
                .normalReplyId(reply.getNormalReplyId())
                .userId(reply.getUser().getUserId())
                .userName(reply.getUser().getName())
                .content(reply.getContent())
                .createdDate(reply.getCreatedDate())
                .build();
    }
}
