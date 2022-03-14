package com.leadpet.www.infrastructure.domain.users;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * LoginMethod
 */
public enum LoginMethod {
    KAKAO("kko") {
        @Override
        public boolean validateEssentialParam(final Users user) {
            if (user.getLoginMethod() != KAKAO) {
                return false;
            }
            if (Objects.isNull(user.getUserType())) {
                return false;
            }
            return !StringUtils.isAnyBlank(user.getUid(), user.getName());
        }
    },
    GOOGLE("ggl") {
        @Override
        public boolean validateEssentialParam(final Users user) {
            if (user.getLoginMethod() != GOOGLE) {
                return false;
            }
            if (Objects.isNull(user.getUserType())) {
                return false;
            }
            return !StringUtils.isAnyBlank(user.getUid(), user.getName());
        }
    },
    APPLE("app") {
        @Override
        public boolean validateEssentialParam(final Users user) {
            if (user.getLoginMethod() != APPLE) {
                return false;
            }
            if (Objects.isNull(user.getUserType())) {
                return false;
            }
            return !StringUtils.isAnyBlank(user.getUid(), user.getName());
        }
    },
    EMAIL("eml") {
        @Override
        public boolean validateEssentialParam(final Users user) {
            if (user.getLoginMethod() != EMAIL) {
                return false;
            }
            if (Objects.isNull(user.getUserType())) {
                return false;
            }
            return !StringUtils.isAnyBlank(
                    user.getUid(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getName());
        }
    };

    @Getter
    private String acronym;
    LoginMethod(String acronym) {
        this.acronym = acronym;
    }

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
