package com.leadpet.www.infrastructure.domain.users;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Users
 */
@Entity
@lombok.Getter
@lombok.Builder
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    // 공통
    private LoginMethod loginMethod;
    private String uid;
    private String email;
    private String password;
    private String profileImage;
    private String name;

    // 보호소
    private String shelterName;
    private String shelterAddress;
    private String shelterPhoneNumber;
    private String shelterManager;
    private String shelterHomePage;

    /**
     * 로그인 방법
     */
    public enum LoginMethod {
        KAKAO,
        GOOGLE,
        APPLE,
        EMAIL;

        @JsonCreator
        public static LoginMethod from(final String loginMethod) {
            return LoginMethod.valueOf(loginMethod.toUpperCase());
        }
    }
}
