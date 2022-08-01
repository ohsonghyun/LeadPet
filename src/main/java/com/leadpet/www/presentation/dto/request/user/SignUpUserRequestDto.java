package com.leadpet.www.presentation.dto.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.leadpet.www.infrastructure.domain.users.LoginMethod;
import com.leadpet.www.infrastructure.domain.users.UserType;
import com.leadpet.www.infrastructure.domain.users.Users;
import io.swagger.annotations.ApiModel;

import javax.validation.constraints.NotNull;

/**
 * SignUpUserRequestDto
 */
@lombok.Getter
@lombok.Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@ApiModel("회원가입 Request")
public class SignUpUserRequestDto {

    // 공통
    @NotNull
    @JsonProperty("loginMethod")
    private LoginMethod loginMethod;
    @NotNull
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
    @NotNull
    @JsonProperty("userType")
    private UserType userType;

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
    @JsonProperty("shelterIntro")
    private String shelterIntro;
    @JsonProperty("shelterAccount")
    private String shelterAccount;

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
                .userType(this.userType)
                .shelterName(this.shelterName)
                .shelterAddress(this.shelterAddress)
                .shelterPhoneNumber(this.shelterPhoneNumber)
                .shelterManager(this.shelterManager)
                .shelterHomePage(this.shelterHomePage)
                .shelterIntro(this.shelterIntro)
                .shelterAccount(this.shelterAccount)
                .build();
    }
}
