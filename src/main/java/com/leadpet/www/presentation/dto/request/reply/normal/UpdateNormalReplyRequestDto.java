package com.leadpet.www.presentation.dto.request.reply.normal;


import io.swagger.annotations.ApiModel;
import lombok.AccessLevel;

/**
 * UpdateNormalReplyRequestDto
 */
@ApiModel("일상피드 댓글 수정 리퀘스트")
@lombok.Getter
@lombok.Builder
@lombok.NoArgsConstructor(access = AccessLevel.PROTECTED)
@lombok.AllArgsConstructor
public class UpdateNormalReplyRequestDto {

    private String userId;
    private String normalReplyId;
    private String newContent;

}
