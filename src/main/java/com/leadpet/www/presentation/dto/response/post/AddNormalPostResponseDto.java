package com.leadpet.www.presentation.dto.response.post;

import com.leadpet.www.infrastructure.domain.posts.NormalPosts;
import io.swagger.annotations.ApiModel;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * AddNormalPostResponseDto
 */
@ApiModel("신규 일반 게시물 Response")
@lombok.Getter
@lombok.Builder(access = lombok.AccessLevel.PRIVATE)
public class AddNormalPostResponseDto {

    private Long normalPostId;
    private String title;
    private String contents;
    private List<String> images;
    private List<String> tags;
    private Long userId;

    public static AddNormalPostResponseDto from(@NonNull final NormalPosts normalPost) {
        return AddNormalPostResponseDto.builder()
                .normalPostId(normalPost.getNormalPostId())
                .title(normalPost.getTitle())
                .contents(normalPost.getContents())
                .images(normalPost.getImages())
                .tags(normalPost.getTags())
                .userId(normalPost.getUserId())
                .build();
    }
}
