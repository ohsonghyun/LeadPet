package com.leadpet.www.application.service;

import com.leadpet.www.infrastructure.db.UsersRepository;
import com.leadpet.www.infrastructure.domain.users.Users;
import org.springframework.stereotype.Service;

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
    public Users saveNewUser(final Users newUser) {
        return usersRepository.save(Users.builder()
                .loginMethod(newUser.getLoginMethod())
                .build());
    }
}
