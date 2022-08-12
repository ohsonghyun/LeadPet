package com.leadpet.www.infrastructure.db.posts.donationPost;

import com.leadpet.www.infrastructure.db.posts.donationPost.condition.SearchDonationPostCondition;
import com.leadpet.www.presentation.dto.response.post.donation.DonationPostPageResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * DonationPostRepositoryCustom
 */
public interface DonationPostRepositoryCustom {
    Page<DonationPostPageResponseDto> searchAll(final SearchDonationPostCondition condition, final Pageable pageable);
}
