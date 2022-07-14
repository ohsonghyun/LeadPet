package com.leadpet.www.presentation.dto.response.post;

import io.swagger.annotations.ApiModel;
import lombok.AccessLevel;

import java.util.List;

/**
 * AllNormalPostsResponse
 */
@ApiModel("일반 게시물 Response")
@lombok.Getter
@lombok.RequiredArgsConstructor
@lombok.Builder(access = AccessLevel.PRIVATE)
public class NormalPostResponse {
    private final String normalPostId;
    private final String title;
    private final String contents;
    private final List<String> images;

    private final String userId;
}
