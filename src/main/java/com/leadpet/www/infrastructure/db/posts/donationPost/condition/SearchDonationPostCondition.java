package com.leadpet.www.infrastructure.db.posts.donationPost.condition;

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
}
