package com.leadpet.www.infrastructure.db.adoptionPost.condition;

import lombok.AccessLevel;

/**
 * SearchAdoptionPostCondition
 */
@lombok.Getter
@lombok.Builder
@lombok.NoArgsConstructor(access = AccessLevel.PROTECTED)
@lombok.AllArgsConstructor
public class SearchAdoptionPostCondition {
    private String userId;
}
