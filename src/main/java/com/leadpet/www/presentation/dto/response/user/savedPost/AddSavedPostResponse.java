package com.leadpet.www.presentation.dto.response.user.savedPost;

import com.leadpet.www.infrastructure.domain.users.savedPost.SavedPost;
import io.swagger.annotations.ApiModel;
import lombok.AccessLevel;
import org.springframework.lang.NonNull;

/**
 * AddSavedPostResponse
 */
@ApiModel("저장피드 추가 Response")
@lombok.Getter
@lombok.Builder(access = AccessLevel.PRIVATE)
public class AddSavedPostResponse {
    private String savedPostId;

    public static AddSavedPostResponse from(@NonNull final SavedPost savedPost) {
        return AddSavedPostResponse.builder()
                .savedPostId(savedPost.getSavedPostId())
                .build();
    }

}
