package com.leadpet.www.presentation.dto.response.post.normal;

import io.swagger.annotations.ApiModel;

import java.time.LocalDateTime;
import java.util.List;

/**
 * SimpleNormalPostResponse
 */
@ApiModel("Simple 일반 게시물 Response")
@lombok.Getter
@lombok.RequiredArgsConstructor
@lombok.Builder
public class SimpleNormalPostResponse {
    private final String normalPostId;
    private final String title;
    private final List<String> images;
    private final String userId;
}
