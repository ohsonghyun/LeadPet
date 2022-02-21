package com.leadpet.www.infrastructure.db;

import com.leadpet.www.infrastructure.domain.users.LoginMethod;
import com.leadpet.www.infrastructure.domain.users.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * UsersRepository
 */
@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    /**
     * 로그인 방법 + UID 조합으로 유저데이터 반
     *
     * @param loginMethod 로그인 방법
     * @param uid 유저 고유 ID
     * @return {@code Users}
     */
    Users findByLoginMethodAndUid(LoginMethod loginMethod, String uid);
}
