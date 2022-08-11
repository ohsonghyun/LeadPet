package com.leadpet.www.presentation.dto.response.user.savedPost;

import io.swagger.annotations.ApiModel;
import lombok.AccessLevel;
import org.springframework.lang.NonNull;

/**
 * DeleteSavedPostResponse
 */
@ApiModel("저장피드 삭제 Response")
@lombok.Getter
@lombok.Builder(access = AccessLevel.PRIVATE)
public class DeleteSavedPostResponse {
    private String savedPostId;

    public static DeleteSavedPostResponse from(@NonNull final String savedPostId) {
        return DeleteSavedPostResponse.builder()
                .savedPostId(savedPostId)
                .build();
    }

}
