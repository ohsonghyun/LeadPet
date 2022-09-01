package com.leadpet.www.infrastructure.domain.users;

import com.leadpet.www.infrastructure.domain.BaseTime;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
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
    @Lob
    @Column(name = "intro")
    private String intro;
    @Column(name = "address")
    private String address;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType userType;

    // 보호소
    // TODO 여유 생기면 테스트와 함께 작성
//    @Embedded
//    private ShelterInfo shelterInfo;
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

    /**
     * 동일한 유저인지 판별
     *
     * @param userId {@code String} 확인하고자 하는 유저ID
     * @return {@code boolean}
     */
    public boolean isSameUser(final String userId) {
        if (StringUtils.isBlank(userId)) {
            throw new IllegalAccessError("userId가 null");
        }
        return this.userId.equals(userId);
    }

    /**
     * 보호소 정보 수정
     *
     * @param newShelterInfo {@code ShelterInfo}
     * @return {@code Users} 수정된 보호소 정보
     */
    public Users updateShelter(final ShelterInfo newShelterInfo) {
        shelterName = newShelterInfo.getShelterName();
        shelterAddress = newShelterInfo.getShelterAddress();
        shelterPhoneNumber = newShelterInfo.getShelterPhoneNumber();
        shelterManager = newShelterInfo.getShelterManager();
        shelterHomePage = newShelterInfo.getShelterHomePage();
        shelterIntro = newShelterInfo.getShelterIntro();
        shelterAccount = newShelterInfo.getShelterAccount();
        return this;
    }

    /**
     * 일반유저 정보 수정
     *
     * @param userInfo {@code UserInfo}
     * @return {@code Users} 수정된 일반유저 정보
     */
    public Users updateNormalUser(final UserInfo userInfo) {
        name = userInfo.getName();
        intro = userInfo.getIntro();
        address = userInfo.getAddress();
        profileImage = userInfo.getProfileImage();
        return this;
    }
}
