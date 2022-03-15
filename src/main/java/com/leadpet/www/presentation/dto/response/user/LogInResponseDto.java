package com.leadpet.www.presentation.dto.response.user;

import com.leadpet.www.infrastructure.domain.users.Users;
import io.swagger.annotations.ApiModel;
import lombok.AccessLevel;
import org.springframework.lang.NonNull;

/**
 * LogInResponseDto
 */
@ApiModel("로그인 Response")
@lombok.Getter
@lombok.Builder(access = AccessLevel.PRIVATE)
public class LogInResponseDto {

    private String uid;
    private String userId;
    private String profileImage;

    public static LogInResponseDto from(@NonNull final Users user) {
        return LogInResponseDto.builder()
                .uid(user.getUid())
                .userId(user.getUserId())
                .profileImage(user.getProfileImage())
                .build();
    }

}
