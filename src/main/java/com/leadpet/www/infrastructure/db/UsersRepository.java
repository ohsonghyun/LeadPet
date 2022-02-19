package com.leadpet.www.infrastructure.db;

import com.leadpet.www.infrastructure.domain.users.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * UsersRepository
 */
@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
}
