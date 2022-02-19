package com.leadpet.www.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.leadpet.www.infrastructure.domain.users.Users;

/**
 * SignUpUserRequestDto
 */
@lombok.Getter
public class SignUpUserRequestDto {

    // 공통
    @JsonProperty("type")
    private Users.LoginMethod loginMethod;
    @JsonProperty("uid")
    private String uid;
    @JsonProperty("email")
    private String email;
    @JsonProperty("password")
    private String password;
    @JsonProperty("profileImage")
    private String profileImage;
    @JsonProperty("name")
    private String name;

    // 보호소
    @JsonProperty("shelterName")
    private String shelterName;
    @JsonProperty("shelterAddress")
    private String shelterAddress;
    @JsonProperty("shelterPhoneNumber")
    private String shelterPhoneNumber;
    @JsonProperty("shelterManager")
    private String shelterManager;
    @JsonProperty("shelterHomePage")
    private String shelterHomePage;

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
                .profileImage(this.profileImage)
                .name(this.name)
                .shelterName(this.shelterName)
                .shelterAddress(this.shelterAddress)
                .shelterPhoneNumber(this.shelterPhoneNumber)
                .shelterManager(this.shelterManager)
                .shelterHomePage(this.shelterHomePage)
                .build();
    }
}
