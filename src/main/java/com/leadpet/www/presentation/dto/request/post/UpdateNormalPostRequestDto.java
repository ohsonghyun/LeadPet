package com.leadpet.www.presentation.dto.request.post;

import com.leadpet.www.infrastructure.domain.posts.NormalPosts;
import com.leadpet.www.infrastructure.domain.users.LoginMethod;
import io.swagger.annotations.ApiModel;

import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel("일반 게시물 수정 Request")
@lombok.Getter
@lombok.Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class UpdateNormalPostRequestDto {

    private Long normalPostId;
    private String title;
    private String contents;
    private List<String> images;
    private List<String> tags;

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
                .normalPostId(this.normalPostId)
                .title(this.title)
                .contents(this.contents)
                .images(this.images)
                .tags(this.tags)
                .userId(this.userId)
                .build();
    }
}
