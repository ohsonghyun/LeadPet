package com.leadpet.www.presentation.dto.response;

import com.leadpet.www.infrastructure.domain.posts.NormalPosts;
import io.swagger.annotations.ApiModel;
import lombok.AccessLevel;
import lombok.NonNull;

import java.util.List;
import java.util.stream.Collectors;

/**
 * AllNormalPostsResponse
 */
@ApiModel("일반 게시물 Response")
@lombok.Getter
@lombok.Builder(access = AccessLevel.PRIVATE)
public class NormalPostResponse {
    private Long normalPostId;
    private String title;
    private String contents;
    private Long userId;

    public static List<NormalPostResponse> from(@NonNull final List<NormalPosts> normalPosts) {
        return normalPosts
                .stream().map(normalPost ->
                        NormalPostResponse.builder()
                                .normalPostId(normalPost.getNormalPostId())
                                .title(normalPost.getTitle())
                                .contents(normalPost.getContents())
                                .userId(normalPost.getUserId())
                                .build())
                .collect(Collectors.toList());
    }
}
