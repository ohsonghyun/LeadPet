package com.leadpet.www.presentation.dto.request.post.donation;

import com.leadpet.www.infrastructure.domain.posts.DonationPosts;
import com.leadpet.www.infrastructure.domain.posts.NormalPosts;
import io.swagger.annotations.ApiModel;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * AddDonationPostRequestDto
 */
@ApiModel("신규 기부 게시물 Request")
@lombok.Getter
@lombok.Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class AddDonationPostRequestDto {

    @NotNull
    private LocalDateTime startDate;
    @NotNull
    private LocalDateTime endDate;
    @NotNull
    private String title;
    @NotNull
    private String donationMethod;
    @NotNull
    private String contents;
    private List<String> images;

    // 유저 판단 데이터
    @NotNull
    private String userId;

    /**
     * 기부 게시물 객체로 변환
     * <p>DonationPosts의 userId는 백엔드에서 취득해서 저장</p>
     *
     * @return {@code DonationPosts}
     */
    public DonationPosts toDontaionPost() {
        return DonationPosts.builder()
                .startDate(this.startDate)
                .endDate(this.endDate)
                .title(this.title)
                .donationMethod(donationMethod)
                .contents(this.contents)
                .images(this.images)
                .build();
    }
}
