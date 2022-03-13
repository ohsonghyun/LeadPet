package com.leadpet.www.presentation.dto.response.user;

import com.leadpet.www.infrastructure.domain.users.LoginMethod;
import com.leadpet.www.infrastructure.domain.users.UserType;
import com.leadpet.www.infrastructure.domain.users.Users;
import io.swagger.annotations.ApiModel;
import lombok.AccessLevel;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.stream.Collectors;

/**
 * UserListResponseDto
 */
@lombok.Getter
@lombok.Builder(access = AccessLevel.PRIVATE)
@ApiModel("유저리스트 취득 Response")
public class UserListResponseDto {

    // 공통
    private LoginMethod loginMethod;
    private String uid;
    private String email;
    private String profileImage;
    private String name;
    private UserType userType;

    // 보호소
    private String shelterName;
    private String shelterAddress;
    private String shelterPhoneNumber;
    private String shelterManager;
    private String shelterHomePage;

    public static List<UserListResponseDto> from(@NonNull final List<Users> userList) {
        return userList
                .stream().map(user ->
                        UserListResponseDto.builder()
                                .loginMethod(user.getLoginMethod())
                                .uid(user.getUid())
                                .email(user.getEmail())
                                .profileImage(user.getProfileImage())
                                .name(user.getName())
                                .userType(user.getUserType())
                                .shelterName(user.getShelterName())
                                .shelterAddress(user.getShelterAddress())
                                .shelterPhoneNumber(user.getShelterPhoneNumber())
                                .shelterManager(user.getShelterManager())
                                .shelterHomePage(user.getShelterHomePage())
                                .build())
                .collect(Collectors.toList());
    }

}
