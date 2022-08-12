package com.leadpet.www.infrastructure.db.posts.normalPost.condition;

import lombok.AccessLevel;
import org.springframework.lang.Nullable;

/**
 * SearchNormalPostCondition
 */
@lombok.Getter
@lombok.Builder
@lombok.NoArgsConstructor(access = AccessLevel.PROTECTED)
@lombok.AllArgsConstructor
public class SearchNormalPostCondition {
    /**
     * 검색 대상 유저ID
     */
    @Nullable
    private String userId;
    /**
     * '좋아요' 정보가 필요한 유저ID
     */
    @Nullable
    private String likedUserId;
}
