package com.leadpet.www.presentation.dto.request.reply.normal;


import io.swagger.annotations.ApiModel;
import lombok.AccessLevel;

/**
 * AddNormalReplyRequestDto
 */
@ApiModel("일상피드 댓글 리퀘스트")
@lombok.Getter
@lombok.Builder
@lombok.NoArgsConstructor(access = AccessLevel.PROTECTED)
@lombok.AllArgsConstructor
public class AddNormalReplyRequestDto {

    private String normalPostId;
    private String userId;
    private String content;

}
