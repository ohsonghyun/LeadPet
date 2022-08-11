package com.leadpet.www.infrastructure.db.users.savedPost;

import com.leadpet.www.presentation.dto.response.post.adoption.SimpleAdoptionPostResponse;
import com.leadpet.www.presentation.dto.response.post.donation.SimpleDonationPostResponse;
import com.leadpet.www.presentation.dto.response.post.normal.SimpleNormalPostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * SavedPostRepositoryCustom
 */
public interface SavedPostRepositoryCustom {
    /**
     * 저장된 일상피드를 userId로 취득
     *
     * @param userId   {@code String}
     * @param pageable {@code Pageable}
     * @return {@code Page<SimpleNormalPostResponse>}
     */
    Page<SimpleNormalPostResponse> findSavedNormalPostsByUserId(final String userId, final Pageable pageable);

    /**
     * 저장된 입양피드를 userId로 취득
     *
     * @param userId   {@code String}
     * @param pageable {@code Pageable}
     * @return {@code Page<SimpleAdoptionPostResponse>}
     */
    Page<SimpleAdoptionPostResponse> findSavedAdoptionPostsByUserId(final String userId, final Pageable pageable);

    /**
     * 저장된 기부피드를 userId로 취득
     *
     * @param userId   {@code String}
     * @param pageable {@code Pageable}
     * @return {@code Page<SimpleDonationPostResponse>}
     */
    Page<SimpleDonationPostResponse> findSavedDonationPostsByUserId(final String userId, final Pageable pageable);


}
