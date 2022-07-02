package com.leadpet.www.infrastructure.db.users;

import com.leadpet.www.infrastructure.db.users.condition.SearchShelterCondition;
import com.leadpet.www.infrastructure.domain.users.Users;
import com.leadpet.www.presentation.dto.response.user.ShelterPageResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * UsersRepositoryCustom
 */
public interface UsersRepositoryCustom {

    /**
     * 보호소 검색
     *
     * @param condition {@code SearchShelterCondition} 검색 조건
     * @param pageable  {@code Pageable}
     * @return {@code <Page<Users>>}
     */
    Page<ShelterPageResponseDto> searchShelters(final SearchShelterCondition condition, final Pageable pageable);
}