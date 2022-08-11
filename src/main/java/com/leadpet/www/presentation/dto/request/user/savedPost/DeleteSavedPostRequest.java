package com.leadpet.www.presentation.dto.request.user.savedPost;

import io.swagger.annotations.ApiModel;

/**
 * DeleteSavedPostRequest
 */
@ApiModel("저장피드 삭제 Request")
@lombok.Getter
@lombok.Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class DeleteSavedPostRequest {
    private String savedPostId;
    private String userId;
}
