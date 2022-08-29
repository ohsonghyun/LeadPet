package com.leadpet.www.infrastructure.db.posts.donationPost.condition;

import com.leadpet.www.infrastructure.domain.donation.DonationMethod;
import lombok.AccessLevel;

/**
 * SearchDonationPostCondition
 */
@lombok.Getter
@lombok.Builder
@lombok.NoArgsConstructor(access = AccessLevel.PROTECTED)
@lombok.AllArgsConstructor
public class SearchDonationPostCondition {
    private String userId;
    private DonationMethod donationMethod;
}
