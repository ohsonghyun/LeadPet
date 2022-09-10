package com.leadpet.www.application.service;

import com.leadpet.www.infrastructure.db.users.UsersRepository;
import com.leadpet.www.infrastructure.db.users.condition.SearchShelterCondition;
import com.leadpet.www.infrastructure.db.users.condition.SearchUserCondition;
import com.leadpet.www.infrastructure.domain.users.*;
import com.leadpet.www.infrastructure.exception.UnsatisfiedRequirementException;
import com.leadpet.www.infrastructure.exception.login.UserNotFoundException;
import com.leadpet.www.infrastructure.exception.signup.UserAlreadyExistsException;
import com.leadpet.www.presentation.dto.response.user.ShelterPageResponseDto;
import com.leadpet.www.presentation.dto.response.user.UserDetailResponseDto;
import com.leadpet.www.presentation.dto.response.user.UserListResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@Transactional(readOnly = true)
@lombok.RequiredArgsConstructor
public class UserService {

    private final UsersRepository usersRepository;

    /**
     * 회원가입
     *
     * @param newUser 새로운 유저 데이터
     * @return 가입 완성 후 유저 데이
     */
    @Transactional
    public Users saveNewUser(@NonNull final Users newUser) {
        if (!newUser.hasAllRequiredValues()) {
            log.error("필수 데이터 누락\tuserId: {}", newUser.getUserId());
            throw new UnsatisfiedRequirementException("Error: 필수 입력 데이터 누락");
        }

        // 로그인 타입이 이메일인 유저는 이메일까지 넘겨받아야하나? 이메일과 비밀번호로만 검색을 해봐야하는지 uid 생성 로직을 확인할 필요가 있음.
        // 일단, 동일한 이메일에서는 동일한 uid가 생성되는걸로 확인 받음. 구현 완료인지는 아직 미확인
        // https://discord.com/channels/933332878232809493/934390730972069938/952454407340060722
        Users userInDb = findUserBy(newUser.getLoginMethod(), newUser.getUid(), newUser.getEmail(), newUser.getPassword());
        if (Objects.nonNull(userInDb)) {
            log.error("이미 존재하는 유저:\tUserId: {}\tuid: {}", userInDb.getUserId(), userInDb.getUid());
            throw new UserAlreadyExistsException("Error: 이미 존재하는 유저");
        }
        newUser.createUserId();
        return usersRepository.save(newUser);
    }

    /**
     * 로그인
     *
     * @param user 로그인 대상 유저
     * @return {@code Users}
     */
    public Users logIn(@NonNull final Users user) {
        Users userInDb = findUserBy(user.getLoginMethod(), user.getUid(), user.getEmail(), user.getPassword());
        if (Objects.isNull(userInDb)) {
            log.info("로그인 실패:\t존재하지 않는 유저:\tuserId: {}\tuid: {}", user.getUserId(), user.getUid());
            throw new UserNotFoundException("Error: 존재하지 않는 유저");
        }
        return userInDb;
    }

    /**
     * 로그인 유형과 UID로 특정 유저를 검색
     *
     * @param loginMethod 로그인 유형
     * @param uid         {@code String}
     * @param email       {@code String}
     * @param password    {@code String}
     * @return {@code Users}
     */
    @Nullable
    Users findUserBy(@NonNull final LoginMethod loginMethod, @NonNull final String uid, @Nullable final String email, @Nullable final String password) {
        if (loginMethod == LoginMethod.EMAIL) {
            if (ObjectUtils.anyNull(email, password)) {
                log.info("Email Login 필수 데이터 누락:\tuid: {}", uid);
                throw new UnsatisfiedRequirementException("Error: 필수 데이터 누락");
            }
            return usersRepository.findByLoginMethodAndUidAndEmailAndPassword(loginMethod, uid, email, password);
        }

        return usersRepository.findByLoginMethodAndUid(loginMethod, uid);
    }

    /**
     * 유저 리스트 취득
     *
     * @param searchUserCondition {@code SearchUserCondition} 유저 리스트 취득 조건
     * @param pageable            {@code Pageable}
     * @return {@code Page<UserListResponseDto>}
     */
    @NonNull
    public Page<UserListResponseDto> searchUsers(SearchUserCondition searchUserCondition, Pageable pageable) {
        Page<UserListResponseDto> usersPage = usersRepository.searchUsers(searchUserCondition, pageable);
        if(usersPage.isEmpty()){
            log.error("[UserService] 존재하지 않는 유저");
            throw new NullPointerException("Error: 존재하지 않는 유저");
        }
        return Objects.isNull(usersPage) ? new PageImpl<>(Collections.EMPTY_LIST) : usersPage;
    }

    /**
     * 보호소 리스트 취득
     *
     * @param searchShelterCondition {@code SearchShelterCondition} 보호소 리스트 취득 조건
     * @param pageable               {@code Pageable}
     * @return {@code Page<ShelterPageResponseDto>}
     */
    @NonNull
    public Page<ShelterPageResponseDto> searchShelters(SearchShelterCondition searchShelterCondition, Pageable pageable) {
        Page<ShelterPageResponseDto> sheltersPage = usersRepository.searchShelters(searchShelterCondition, pageable);
        return Objects.isNull(sheltersPage) ? new PageImpl<>(Collections.EMPTY_LIST) : sheltersPage;
    }

    /**
     * 보호소 디테일 취득
     *
     * @param userId 보호소 유저 ID
     * @return {@code Users}
     */
    @NonNull
    public Users shelterDetail(final String userId) {
        // 여기에 들어올 가능성은 희박하지만..
        if (StringUtils.isBlank(userId)) {
            log.error("[UserService] userId가 null");
            throw new UnsatisfiedRequirementException("Error: 필수 데이터 부족");
        }
        Users shelter = usersRepository.findShelterByUserId(userId);
        if (Objects.isNull(shelter)) {
            log.error("[UserService] 존재하지 않는 보호소: {}", userId);
            throw new UserNotFoundException("Error: 존재하지 않는 보호소");
        }
        return shelter;
    }

    /**
     * 보호소 정보 수정
     *
     * @param newShelterInfo {@code ShelterInfo}
     * @return {@code Users} 수정된 유저 데이터
     */
    @Transactional
    public Users updateShetlerInfo(
            @NonNull final String userId,
            @NonNull final ShelterInfo newShelterInfo
    ) {
        Users targetShelter = usersRepository.findShelterByUserId(userId);
        if (Objects.isNull(targetShelter)) {
            log.error("[UserServier#updateShelterInfo] 존재하지 않는 유저ID: {}", userId);
            throw new UserNotFoundException("Error: 존재하지 않는 유저Id");
        }
        return targetShelter.updateShelter(newShelterInfo);
    }

    /**
     * 일반유저 정보 수정
     *
     * @param newUserInfo {@code UserInfo}
     * @return {@code Users} 수정된 유저 데이터
     */
    @Transactional
    public Users updateNormalUser(
            @NonNull final String userId,
            @NonNull final UserInfo newUserInfo
    ) {
        Users targetNormalUser = usersRepository.findNormalUserByUserId(userId);
        if (Objects.isNull(targetNormalUser)) {
            log.error("[UserServier#updateNormalUser] 존재하지 않는 유저ID: {}", userId);
            throw new UserNotFoundException("Error: 존재하지 않는 유저Id");
        }
        return targetNormalUser.updateNormalUser(newUserInfo);
    }

    /**
     * 일반 유저 디테일 취득
     *
     * @param userId {@code String}
     * @return {@code UserDetailResponseDto}
     */
    public UserDetailResponseDto normalUserDetail(final String userId) {
        // 여기에 들어올 가능성은 희박하지만..
        if (StringUtils.isBlank(userId)) {
            log.error("[UserService] userId가 null");
            throw new UnsatisfiedRequirementException("Error: 필수 데이터 부족");
        }
        UserDetailResponseDto normalUser = usersRepository.findNormalUserDetailByUserId(userId);
        if (Objects.isNull(normalUser)) {
            log.error("[UserService] 존재하지 않는 유저: {}", userId);
            throw new UserNotFoundException("Error: 존재하지 않는 유저");
        }
        return normalUser;
    }
}
