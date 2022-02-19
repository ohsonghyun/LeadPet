package com.leadpet.www.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.leadpet.www.infrastructure.domain.users.Users;

@lombok.Getter
public class SignUpUserDto {

    @JsonProperty("type")
    private Users.LoginMethod loginMethod;

    /**
     * Users 객체로 변환
     *
     * @return {@code Users}
     */
    public Users toUsers() {
        return Users.builder()
                .loginMethod(this.loginMethod)
                .build();
    }
}
