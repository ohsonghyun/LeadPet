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

    /**
     * EMAIL 로그인 유형의 로그인
     * <p>로그인 방법이 EMAIL인 경우, 로그인 방법이 SNS와 다름</p>
     *
     * @param loginMethod 로그인 방법
     * @param uid 유저 고유 ID
     * @param email 이메일
     * @param password 패스워드
     * @return {@code Users}
     */
    Users findByLoginMethodAndUidAndEmailAndPassword(LoginMethod loginMethod, String uid, String email, String password);
}
