package com.leadpet.www.presentation.dto.request.post;

import com.leadpet.www.infrastructure.domain.posts.NormalPosts;
import io.swagger.annotations.ApiModel;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * AddNormalPostRequestDto
 */
@ApiModel("신규 일반 게시물 Request")
@lombok.Getter
@lombok.Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class AddNormalPostRequestDto {

    @NotNull
    private String title;
    @NotNull
    private String contents;
    private List<String> images;

    // 유저 판단 데이터
    @NotNull
    private String userId;

    /**
     * 일반 게시물 객체로 변환
     * <p>NormalPosts의 userId는 백엔드에서 취득해서 저장</p>
     *
     * @return {@code NormalPosts}
     */
    public NormalPosts toNormalPost() {
        return NormalPosts.builder()
                .title(this.title)
                .contents(this.contents)
                .images(this.images)
                .build();
    }
}
