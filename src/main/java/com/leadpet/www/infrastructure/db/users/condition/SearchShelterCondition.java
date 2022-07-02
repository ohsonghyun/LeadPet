package com.leadpet.www.infrastructure.db.users.condition;

import lombok.AccessLevel;

/**
 * SearchShelterCondition
 */
@lombok.Getter
@lombok.Builder
@lombok.NoArgsConstructor(access = AccessLevel.PROTECTED)
@lombok.AllArgsConstructor
public class SearchShelterCondition {
    private String cityName;
    private String shelterName;
}
