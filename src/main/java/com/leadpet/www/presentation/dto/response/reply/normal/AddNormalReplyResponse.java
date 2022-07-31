package com.leadpet.www.presentation.dto.response.reply.normal;

import com.leadpet.www.infrastructure.domain.breed.Breed;
import com.leadpet.www.infrastructure.domain.reply.normal.NormalReply;
import io.swagger.annotations.ApiModel;

import java.time.LocalDateTime;

/**
 * AddNormalReplyResponse
 */
@ApiModel("일상피드 댓글 추가 Response")
@lombok.Getter
@lombok.Builder(access = lombok.AccessLevel.PRIVATE)
public class AddNormalReplyResponse {
    private String normalReplyId;
    private String userId;
    private String userName;
    private String content;
    private LocalDateTime createdDate;

    public static AddNormalReplyResponse from(final NormalReply reply) {
        return AddNormalReplyResponse.builder()
                .normalReplyId(reply.getNormalReplyId())
                .userId(reply.getUser().getUserId())
                .userName(reply.getUser().getName())
                .content(reply.getContent())
                .createdDate(reply.getCreatedDate())
                .build();
    }
}
