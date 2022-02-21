package com.leadpet.www.infrastructure.domain.users;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

/**
 * Users
 */
@Entity
@lombok.Getter
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.Builder
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    // 공통
    private LoginMethod loginMethod;
    private String uid;
    private String email;
    private String password;
    private String profileImage;
    private String name;

    // 보호소
    private String shelterName;
    private String shelterAddress;
    private String shelterPhoneNumber;
    private String shelterManager;
    private String shelterHomePage;

    /**
     * 셀프체크
     * <p>로그인 유형별 필수 데이터 체크</p>
     *
     * @return {@code boolean}
     */
    public boolean hasAllRequiredValues() {
        if (Objects.nonNull((this.loginMethod))) {
            return this.loginMethod.validateEssentialParam(this);
        }
        return false;
    }
}
