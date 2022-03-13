package com.leadpet.www.infrastructure.domain.users;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.leadpet.www.infrastructure.domain.BaseTime;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

/**
 * Users
 */
@Entity
@lombok.Getter
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.Builder
public class Users extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    // 공통
    @Column(nullable = false)
    private LoginMethod loginMethod;
    @Column(nullable = false)
    private String uid;
    private String email;
    private String password;
    private String profileImage;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private UserType userType;

    // 보호소
    private String shelterName;
    private String shelterAddress;
    private String shelterPhoneNumber;
    private String shelterManager;
    private String shelterHomePage;

    /**
     * 셀프체크
     * <p>로그인 유형별 필수 데이터 체크</p>
     *
     * @return {@code boolean}
     */
    public boolean hasAllRequiredValues() {
        if (ObjectUtils.allNotNull(this.loginMethod, this.userType)) {
            return this.loginMethod.validateEssentialParam(this) && this.userType.validateEssentialParam(this);
        }
        return false;
    }

    /**
     * 유저 유형
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
}
