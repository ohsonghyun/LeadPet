package com.leadpet.www.application.service;

import com.leadpet.www.infrastructure.db.UsersRepository;
import com.leadpet.www.infrastructure.domain.users.Users;
import com.leadpet.www.infrastructure.error.signup.UserAlreadyExistsException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

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
        Users userInDb = usersRepository.findByLoginMethodAndUid(newUser.getLoginMethod(), newUser.getUid());
        if (Objects.nonNull(userInDb)) {
            throw new UserAlreadyExistsException("Error: 이미 존재하는 유저");
        }
        return usersRepository.save(newUser);
    }
}
