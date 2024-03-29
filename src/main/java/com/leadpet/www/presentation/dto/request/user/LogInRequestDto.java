package com.leadpet.www.presentation.dto.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.leadpet.www.infrastructure.domain.users.LoginMethod;
import com.leadpet.www.infrastructure.domain.users.UserType;
import com.leadpet.www.infrastructure.domain.users.Users;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;

/**
 * LogInRequestDto
 */
@lombok.Getter
@lombok.Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@ApiModel("로그인 Request")
public class LogInRequestDto {

    // sns 로그인
    @NotNull
    @JsonProperty("loginMethod")
    private LoginMethod loginMethod;
    @NotNull
    @JsonProperty("uid")
    private String uid;
    @NotNull
    @JsonProperty("userType")
    private UserType userType;

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

    public boolean checkAdmin() {
        return this.userType == UserType.ADMIN ? (this.loginMethod == LoginMethod.EMAIL ? true : false) :false;
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
                .userType(this.userType)
                .build();
    }
}
