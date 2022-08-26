package com.leadpet.www.presentation.dto.request.user;

import com.leadpet.www.infrastructure.domain.users.UserInfo;
import lombok.AccessLevel;

/**
 * UpdateUserInfoRequestDto
 */
@lombok.Getter
@lombok.Builder
@lombok.NoArgsConstructor(access = AccessLevel.PROTECTED)
@lombok.AllArgsConstructor
public class UpdateUserInfoRequestDto {

    private String name;
    private String intro;
    private String address;
    private String profileImage;

    /**
     * {@code UserInfo} 객체로 변환
     */
    public UserInfo toUserInfo() {
        return UserInfo.builder()
                .name(name)
                .intro(intro)
                .address(address)
                .profileImage(profileImage)
                .build();
    }
}
