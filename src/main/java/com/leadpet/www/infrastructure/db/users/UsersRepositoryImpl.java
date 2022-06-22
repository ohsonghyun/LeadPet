package com.leadpet.www.infrastructure.db.users;

import com.leadpet.www.infrastructure.db.users.condition.SearchShelterCondition;
import com.leadpet.www.infrastructure.domain.users.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * UsersRepositoryImpl
 */
public class UsersRepositoryImpl implements UsersRepositoryCustom {
    @Override
    public Page<Users> searchShelters(SearchShelterCondition condition, Pageable pageable) {
        return null;
    }
}
