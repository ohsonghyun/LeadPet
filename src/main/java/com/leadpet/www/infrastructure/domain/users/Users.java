package com.leadpet.www.infrastructure.domain.users;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AccessLevel;

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
    private LoginMethod loginMethod;
    // TODO ... 필요한 필드 정의하기

    /**
     * 로그인 방법
     */
    public enum LoginMethod {
        KAKAO,
        GOOGLE,
        EMAIL,
        APPLE;

        @JsonCreator
        public static LoginMethod from(final String loginMethod) {
            return LoginMethod.valueOf(loginMethod.toUpperCase());
        }
    }
}
