package com.leadpet.www.infrastructure.domain.users;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * UserType
 */
public enum UserType {
    NORMAL() {
        @Override
        public boolean validateEssentialParam(final Users user) {
            // 필수사항은 LoginMethod에서 검사
            return true;
        }
    },
    SHELTER() {
        @Override
        public boolean validateEssentialParam(final Users user) {
            return !StringUtils.isAnyBlank(
                    user.getShelterName(),
                    user.getShelterAddress(),
                    user.getShelterPhoneNumber()
            );
        }
    },
    ;

    @JsonCreator
    public static UserType from(final String userType) {
        return UserType.valueOf(userType.toUpperCase());
    }

    /**
     * 문자열에 해당하는 유저 유형이 존재하는지 확인
     *
     * @param userType 유저 유형에 해당하는 문자열
     * @return {@code boolean}
     */
    public static boolean has(final String userType) {
        return Arrays.stream(values()).anyMatch(value -> value.name().equalsIgnoreCase(userType));
    }

    /**
     * 유저 유형별 필수 데이터 확인
     *
     * @param user 유저 데이터
     * @return {@code boolean}
     */
    abstract public boolean validateEssentialParam(final Users user);
}
