package com.leadpet.www.infrastructure.db.users.condition;

import com.leadpet.www.infrastructure.domain.users.UserType;
import lombok.AccessLevel;

@lombok.Getter
@lombok.Builder
@lombok.NoArgsConstructor(access = AccessLevel.PROTECTED)
@lombok.AllArgsConstructor
public class SearchUserCondition {
    private UserType userType;
}
