package com.leadpet.www.infrastructure.db.adoptionPost;

import com.leadpet.www.presentation.dto.response.post.adoption.AdoptionPostPageResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * AdoptionPostRepositoryCustom
 */
public interface AdoptionPostRepositoryCustom {
    Page<AdoptionPostPageResponseDto> searchAll(Pageable pageable);
}