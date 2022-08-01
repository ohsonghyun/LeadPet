package com.leadpet.www.infrastructure.domain.users;

import com.leadpet.www.infrastructure.domain.BaseTime;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.lang.NonNull;

import javax.persistence.*;

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
    @Column(name = "user_id")
    private String userId;

    // 공통
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LoginMethod loginMethod;
    @Column(nullable = false)
    private String uid;
    private String email;
    private String password;
    private String profileImage;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType userType;

    // 보호소
    private String shelterName;
    private String shelterAddress;
    private String shelterPhoneNumber;
    private String shelterManager;
    private String shelterHomePage;
    private String shelterIntro;
    private String shelterAccount;

    // TODO 이거 내부적으로 설정하도록 해야함.
    @Column(name = "shelter_assessment_status")
    @Enumerated(value = EnumType.STRING)
    private AssessmentStatus shelterAssessmentStatus;

    /**
     * UserId를 생성
     * <p>유저아이디는 uid + loginMethod(약자)로 구성됨</p>
     *
     * @return {@code String}
     */
    @NonNull
    public void createUserId() {
        this.userId = String.format("%s%s", this.uid, this.loginMethod.getAcronym());
    }

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
