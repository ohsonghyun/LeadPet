package com.leadpet.www.infrastructure.db.adoptionPost;

import com.leadpet.www.presentation.dto.response.post.adoption.AdoptionPostPageResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * AdoptionPostRepositoryCustom
 */
public interface AdoptionPostRepositoryCustom {
    /**
     * 모든 입양 피드를 반환 (페이지네이션)
     *
     * @param pageable
     * @return {@code Page<AdoptionPostPageResponseDto>}
     */
    Page<AdoptionPostPageResponseDto> searchAll(Pageable pageable);
}
