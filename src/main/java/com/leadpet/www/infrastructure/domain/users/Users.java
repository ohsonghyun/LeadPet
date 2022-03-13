package com.leadpet.www.infrastructure.domain.users;

import com.leadpet.www.infrastructure.domain.BaseTime;
import com.leadpet.www.infrastructure.domain.posts.NormalPosts;
import org.apache.commons.lang3.ObjectUtils;

import javax.persistence.*;
import java.util.List;

/**
 * Users
 */
@Entity
@lombok.Getter
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.Builder
public class Users extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    // 공통
    @Column(nullable = false)
    private LoginMethod loginMethod;
    @Column(nullable = false)
    private String uid;
    private String email;
    private String password;
    private String profileImage;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private UserType userType;

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
        if (ObjectUtils.allNotNull(this.loginMethod, this.userType)) {
            return this.loginMethod.validateEssentialParam(this) && this.userType.validateEssentialParam(this);
        }
        return false;
    }
}
