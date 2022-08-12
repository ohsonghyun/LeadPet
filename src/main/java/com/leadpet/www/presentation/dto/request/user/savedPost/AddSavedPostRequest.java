package com.leadpet.www.presentation.dto.request.user.savedPost;

import com.leadpet.www.infrastructure.domain.posts.PostType;
import io.swagger.annotations.ApiModel;

/**
 * AddSavedPostRequest
 */
@ApiModel("저장피드 추가 Request")
@lombok.Getter
@lombok.Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class AddSavedPostRequest {
    private String postId;
    private PostType postType;
    private String userId;
}
