package com.leadpet.www.infrastructure.db.posts.adoptionPost;

import com.leadpet.www.infrastructure.db.posts.adoptionPost.condition.SearchAdoptionPostCondition;
import com.leadpet.www.presentation.dto.response.post.adoption.AdoptionPostPageResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * AdoptionPostRepositoryCustom
 */
public interface AdoptionPostRepositoryCustom {
    /**
     * 조건에 맞는 모든 입양 피드를 반환 (페이지네이션)
     *
     * @param condition {@code SearchAdoptionPostCodition}
     * @param pageable  {@code Pageable}
     * @return {@code Page<AdoptionPostPageResponseDto>}
     */
    Page<AdoptionPostPageResponseDto> searchAll(SearchAdoptionPostCondition condition, Pageable pageable);
}
