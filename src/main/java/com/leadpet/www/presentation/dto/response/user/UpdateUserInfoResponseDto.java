package com.leadpet.www.presentation.dto.response.user;

import com.leadpet.www.infrastructure.domain.users.Users;
import io.swagger.annotations.ApiModel;
import lombok.AccessLevel;

/**
 * UpdateUserInfoResponseDto
 */
@ApiModel("일반유저 정보 수정 Response")
@lombok.Getter
@lombok.AllArgsConstructor(access = AccessLevel.PRIVATE)
@lombok.Builder
public class UpdateUserInfoResponseDto {

    private String userId;
    private String name;
    private String intro;
    private String address;
    private String profileImage;


    /**
     * Users 객체로부터 DTO 생성
     *
     * @param user {@code Users}
     * @return {@code UpdateUserInfoResponseDto}
     */
    public static UpdateUserInfoResponseDto from(final Users user) {
        return UpdateUserInfoResponseDto.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .intro(user.getIntro())
                .address(user.getAddress())
                .profileImage(user.getProfileImage())
                .build();
    }
}
