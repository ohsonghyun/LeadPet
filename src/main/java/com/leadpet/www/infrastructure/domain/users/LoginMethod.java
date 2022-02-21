package com.leadpet.www.infrastructure.domain.users;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.apache.commons.lang3.StringUtils;

/**
 * LoginMethod
 */
public enum LoginMethod {
    KAKAO() {
        @Override
        public boolean validateEssentialParam(final Users user) {
            if (user.getLoginMethod() != KAKAO) {
                return false;
            }
            return !StringUtils.isAnyBlank(user.getUid(), user.getName());
        }
    },
    GOOGLE() {
        @Override
        public boolean validateEssentialParam(final Users user) {
            if (user.getLoginMethod() != GOOGLE) {
                return false;
            }
            return !StringUtils.isAnyBlank(user.getUid(), user.getName());
        }
    },
    APPLE() {
        @Override
        public boolean validateEssentialParam(final Users user) {
            if (user.getLoginMethod() != APPLE) {
                return false;
            }
            return !StringUtils.isAnyBlank(user.getUid(), user.getName());
        }
    },
    EMAIL() {
        @Override
        public boolean validateEssentialParam(final Users user) {
            if (user.getLoginMethod() != EMAIL) {
                return false;
            }
            return !StringUtils.isAnyBlank(
                    user.getUid(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getName());
        }
    };

    @JsonCreator
    public static LoginMethod from(final String loginMethod) {
        return LoginMethod.valueOf(loginMethod.toUpperCase());
    }

    /**
     * 유저 로그인 유형별 필요한 데이터 유무 확인
     * @param user 유저 데이터
     * @return {@code boolean}
     */
    abstract public boolean validateEssentialParam(final Users user);
}
