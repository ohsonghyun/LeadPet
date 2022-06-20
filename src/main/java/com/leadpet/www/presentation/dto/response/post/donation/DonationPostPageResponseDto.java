package com.leadpet.www.presentation.dto.response.post.donation;

import io.swagger.annotations.ApiModel;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DonationPostPageResponseDto
 */
@ApiModel("기부 게시물 페이지네이션 Response")
@lombok.Getter
@lombok.RequiredArgsConstructor
public class DonationPostPageResponseDto {
    private final String donationPostId;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final String title;
    private final String donationMethod;
    private final String contents;
    private final List<String> images;
    private final String userId;
}
