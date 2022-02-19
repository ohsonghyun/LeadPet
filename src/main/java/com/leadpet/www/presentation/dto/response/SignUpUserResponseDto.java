package com.leadpet.www.presentation.dto.response;

import lombok.AccessLevel;
import org.springframework.lang.NonNull;

/**
 * SignUpUserResponseDto
 */
@lombok.Getter
@lombok.Builder(access = AccessLevel.PRIVATE)
public class SignUpUserResponseDto {

    private String uid;

    public static SignUpUserResponseDto from(@NonNull final String uid) {
        return SignUpUserResponseDto.builder()
                .uid(uid)
                .build();
    }

}
