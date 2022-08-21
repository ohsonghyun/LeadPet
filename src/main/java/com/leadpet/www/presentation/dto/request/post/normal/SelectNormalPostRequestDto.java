package com.leadpet.www.presentation.dto.request.post.normal;

import com.leadpet.www.infrastructure.domain.posts.NormalPosts;
import io.swagger.annotations.ApiModel;
import lombok.AccessLevel;

import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel("일반 게시물 상세조회 Response")
@lombok.Getter
@lombok.AllArgsConstructor(access = AccessLevel.PRIVATE)
@lombok.Builder
public class SelectNormalPostRequestDto {

    private String normalPostId;
    private String title;
    private String contents;
    private List<String> images;

    @NotNull
    private String userId;

    /**
     * 일반 게시물 객체로 변환
     *
     * @return {@code NormalPosts}
     */
    public static SelectNormalPostRequestDto from(final NormalPosts normalPosts) {
        return SelectNormalPostRequestDto.builder()
                .normalPostId(normalPosts.getNormalPostId())
                .title(normalPosts.getTitle())
                .contents(normalPosts.getContents())
                .images(normalPosts.getImages())
                .userId(normalPosts.getUser().getUserId())
                .build();
    }
}
