package com.leadpet.www.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.leadpet.www.infrastructure.domain.users.LoginMethod;
import com.leadpet.www.infrastructure.domain.users.Users;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;

/**
 * LogInRequestDto
 */
@lombok.Getter
@lombok.Builder
public class LogInRequestDto {

    // sns 로그인
    @NotNull
    @JsonProperty("type")
    private LoginMethod loginMethod;
    @NotNull
    @JsonProperty("uid")
    private String uid;

    // email 로그인: loginMethod + uid + email + password
    @JsonProperty("email")
    private String email;
    @JsonProperty("password")
    private String password;

    /**
     * 필수 데이터 취득 여부 확인
     * @return {@code boolean}
     */
    public boolean hasAllRequiredValue() {
        if (this.loginMethod == LoginMethod.EMAIL) {
            return StringUtils.isNoneBlank(this.email, this.password);
        }
        return true;
    }

    /**
     * Users 객체로 변환
     *
     * @return {@code Users}
     */
    public Users toUsers() {
        return Users.builder()
                .loginMethod(this.loginMethod)
                .uid(this.uid)
                .email(this.email)
                .password(this.password)
                .build();
    }
}
