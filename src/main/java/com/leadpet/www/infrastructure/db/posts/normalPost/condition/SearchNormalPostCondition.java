package com.leadpet.www.infrastructure.db.posts.normalPost.condition;

import lombok.AccessLevel;

/**
 * SearchNormalPostCondition
 */
@lombok.Getter
@lombok.Builder
@lombok.NoArgsConstructor(access = AccessLevel.PROTECTED)
@lombok.AllArgsConstructor
public class SearchNormalPostCondition {
    private String userId;
}
