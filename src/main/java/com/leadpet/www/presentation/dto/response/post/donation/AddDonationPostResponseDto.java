package com.leadpet.www.presentation.dto.response.post.donation;

import com.leadpet.www.infrastructure.domain.donation.DonationMethod;
import com.leadpet.www.infrastructure.domain.posts.DonationPosts;
import io.swagger.annotations.ApiModel;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.List;

/**
 * AddDonationPostResponseDto
 */
@ApiModel("신규 기부 게시물 Response")
@lombok.Getter
@lombok.Builder(access = lombok.AccessLevel.PRIVATE)
public class AddDonationPostResponseDto {

    private String donationPostId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private DonationMethod donationMethod;
    private String title;
    private String contents;
    private List<String> images;
    private String userId;

    public static AddDonationPostResponseDto from(@NonNull final DonationPosts donationPost) {
        return AddDonationPostResponseDto.builder()
                .donationPostId(donationPost.getDonationPostId())
                .startDate(donationPost.getStartDate())
                .endDate(donationPost.getEndDate())
                .donationMethod(donationPost.getDonationMethod())
                .title(donationPost.getTitle())
                .contents(donationPost.getContents())
                .images(donationPost.getImages())
                .userId(donationPost.getUser().getUserId())
                .build();
    }
}
