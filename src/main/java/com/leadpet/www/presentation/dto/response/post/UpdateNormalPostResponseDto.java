package com.leadpet.www.presentation.dto.response.post;

import com.leadpet.www.infrastructure.domain.posts.NormalPosts;
import com.leadpet.www.infrastructure.domain.users.LoginMethod;
import io.swagger.annotations.ApiModel;
import lombok.AccessLevel;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * UpdateNormalPostResponseDto
 */
@ApiModel("일반 게시물 수정 Response")
@lombok.Getter
@lombok.Builder(access = AccessLevel.PRIVATE)
public class UpdateNormalPostResponseDto {

    private Long normalPostId;
    private String title;
    private String contents;
    private List<String> images;
    private List<String> tags;

    private LoginMethod loginMethod;
    private String uid;

    public static UpdateNormalPostResponseDto from(@NonNull final NormalPosts updatedNormalPost,
                                                   @NonNull final LoginMethod loginMethod,
                                                   @NonNull final String uid) {
        return UpdateNormalPostResponseDto.builder()
                .normalPostId(updatedNormalPost.getNormalPostId())
                .title(updatedNormalPost.getTitle())
                .contents(updatedNormalPost.getContents())
                .images(updatedNormalPost.getImages())
                .tags(updatedNormalPost.getTags())
                .loginMethod(loginMethod)
                .uid(uid)
                .build();
    }
}
