package com.leadpet.www.infrastructure.domain.users;

import lombok.AccessLevel;

import javax.persistence.*;

/**
 * UserInfo
 */
@Embeddable
@lombok.Getter
@lombok.Builder
@lombok.NoArgsConstructor(access = AccessLevel.PROTECTED)
@lombok.AllArgsConstructor
public class UserInfo {
    @Column(name = "name", nullable = false)
    private String name;
    @Lob
    @Column(name = "intro")
    private String intro;
    @Column(name = "address")
    private String address;
    @Column(name = "profile_image")
    private String profileImage;
}
