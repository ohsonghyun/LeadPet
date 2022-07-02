package com.leadpet.www.application.service;

import com.leadpet.www.infrastructure.db.users.UsersRepository;
import com.leadpet.www.infrastructure.db.users.condition.SearchShelterCondition;
import com.leadpet.www.infrastructure.domain.users.LoginMethod;
import com.leadpet.www.infrastructure.domain.users.UserType;
import com.leadpet.www.infrastructure.domain.users.Users;
import com.leadpet.www.infrastructure.exception.UnsatisfiedRequirementException;
import com.leadpet.www.infrastructure.exception.login.UserNotFoundException;
import com.leadpet.www.infrastructure.exception.signup.UserAlreadyExistsException;
import com.leadpet.www.presentation.dto.response.user.ShelterPageResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@lombok.RequiredArgsConstructor
public class UserService {

    private final UsersRepository usersRepository;

    /**
     * 회원가입
     *
     * @param newUser 새로운 유저 데이터
     * @return 가입 완성 후 유저 데이
     */
    public Users saveNewUser(@NonNull final Users newUser) {
        if (!newUser.hasAllRequiredValues()) {
            throw new UnsatisfiedRequirementException("Error: 필수 입력 데이터 누락");
        }

        // 로그인 타입이 이메일인 유저는 이메일까지 넘겨받아야하나? 이메일과 비밀번호로만 검색을 해봐야하는지 uid 생성 로직을 확인할 필요가 있음.
        // 일단, 동일한 이메일에서는 동일한 uid가 생성되는걸로 확인 받음. 구현 완료인지는 아직 미확인
        // https://discord.com/channels/933332878232809493/934390730972069938/952454407340060722
        Users userInDb = findUserBy(newUser.getLoginMethod(), newUser.getUid(), newUser.getEmail(), newUser.getPassword());
        if (Objects.nonNull(userInDb)) {
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
                throw new UnsatisfiedRequirementException("Error: 필수 데이터 누락");
            }
            return usersRepository.findByLoginMethodAndUidAndEmailAndPassword(loginMethod, uid, email, password);
        }

        return usersRepository.findByLoginMethodAndUid(loginMethod, uid);
    }

    /**
     * 유저 타입별 리스트 획득
     *
     * @param userType 획득하려고하는 유저 타입
     * @return {@code List<Users>}
     */
    @NonNull
    public List<Users> getUserListBy(@NonNull final UserType userType) {
        return usersRepository.findByUserType(userType);
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
}
