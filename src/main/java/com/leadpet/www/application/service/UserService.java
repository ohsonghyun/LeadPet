package com.leadpet.www.application.service;

import com.leadpet.www.infrastructure.db.UsersRepository;
import com.leadpet.www.infrastructure.domain.users.LoginMethod;
import com.leadpet.www.infrastructure.domain.users.UserType;
import com.leadpet.www.infrastructure.domain.users.Users;
import org.apache.commons.lang3.ObjectUtils;
import com.leadpet.www.infrastructure.exception.login.UserNotFoundException;
import com.leadpet.www.infrastructure.exception.UnsatisfiedRequirementException;
import com.leadpet.www.infrastructure.exception.signup.UserAlreadyExistsException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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

        // TODO 로그인 타입이 이메일인 유저는 이메일까지 넘겨받아야하나? 이메일과 비밀번호로만 검색을 해봐야하는지 uid 생성 로직을 확인할 필요가 있음.
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
        // TODO 이놈.. 실수네 중복. DRY 위반
        return usersRepository.findByLoginMethodAndUid(user.getLoginMethod(), user.getUid());
    }

    /**
     * 로그인 유형과 UID로 특정 유저를 검색
     *
     * @param loginMethod 로그인 유형
     * @param uid {@code String}
     * @param email {@code String}
     * @param password {@code String}
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
}
