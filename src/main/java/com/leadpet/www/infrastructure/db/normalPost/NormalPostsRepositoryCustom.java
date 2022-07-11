package com.leadpet.www.infrastructure.db.normalPost;

import com.leadpet.www.presentation.dto.response.post.NormalPostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * NormalPostsRepositoryCustom
 */
public interface NormalPostsRepositoryCustom {

    /**
     * 모든 일반 피드를 반환 (페이지네이션)
     *
     * @param pageable
     * @return {@code Page<NormalPostResponse>}
     */
    Page<NormalPostResponse> searchAll(final Pageable pageable);

}
