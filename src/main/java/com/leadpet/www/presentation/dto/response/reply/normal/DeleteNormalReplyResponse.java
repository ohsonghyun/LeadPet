package com.leadpet.www.presentation.dto.response.reply.normal;

import io.swagger.annotations.ApiModel;

/**
 * DeleteNormalReplyResponse
 */
@ApiModel("일상피드 댓글 삭제 Response")
@lombok.Getter
@lombok.Builder(access = lombok.AccessLevel.PRIVATE)
public class DeleteNormalReplyResponse {
    private String normalReplyId;

    public static DeleteNormalReplyResponse from(final String replyId) {
        return DeleteNormalReplyResponse.builder()
                .normalReplyId(replyId)
                .build();
    }
}
