package com.leadpet.www.presentation.dto.response.post.donation;

import com.leadpet.www.infrastructure.domain.donation.DonationMethod;
import io.swagger.annotations.ApiModel;

import java.time.LocalDateTime;
import java.util.List;

/**
 * SimpleDonationPostResponse
 */
@ApiModel("Simple 기부 게시물 Response")
@lombok.Getter
@lombok.RequiredArgsConstructor
@lombok.Builder
public class SimpleDonationPostResponse {
    private final String donationPostId;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final String title;
    private final DonationMethod donationMethod;
    private final String contents;
    private final List<String> images;
    private final String userId;
    private final String userName;
    private final String profileImage;
}
