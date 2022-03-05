package com.leadpet.www.infrastructure.domain.users;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.persistence.*;
import java.util.Objects;

/**
 * Users
 */
@Entity
@lombok.Getter
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.Builder
public class Users {
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
        if (Objects.nonNull((this.loginMethod))) {
            return this.loginMethod.validateEssentialParam(this);
        }
        return false;
    }

    /**
     * 유저 유형
     */
    public enum UserType {
        NORMAL,
        SHELTER,
        ;

        @JsonCreator
        public UserType from(final String userType) {
            return UserType.valueOf(userType.toUpperCase());
        }
    }
}
