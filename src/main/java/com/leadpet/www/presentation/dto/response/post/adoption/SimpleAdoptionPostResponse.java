package com.leadpet.www.presentation.dto.response.post.adoption;

import io.swagger.annotations.ApiModel;

import java.util.List;

/**
 * SimpleAdoptionPostResponse
 */
@ApiModel("Simple 입양 게시물 Response")
@lombok.Getter
@lombok.RequiredArgsConstructor
@lombok.Builder
public class SimpleAdoptionPostResponse {
    private final String adoptionPostId;
    private final String title;
    private final List<String> images;
    private final String userId;
}
