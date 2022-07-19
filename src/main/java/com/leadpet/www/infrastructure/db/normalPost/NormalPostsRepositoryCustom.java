package com.leadpet.www.infrastructure.db.normalPost;

import com.leadpet.www.infrastructure.db.normalPost.condition.SearchNormalPostCondition;
import com.leadpet.www.presentation.dto.response.post.NormalPostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * NormalPostsRepositoryCustom
 */
public interface NormalPostsRepositoryCustom {

    /**
     * 조건에 맞는 모든 일반 피드를 반환 (페이지네이션)
     *
     * @param condition {@code SearchNormalPostCondition}
     * @param pageable  {@code Pageable}
     * @return {@code Page<NormalPostResponse>}
     */
    Page<NormalPostResponse> searchAll(final SearchNormalPostCondition condition, final Pageable pageable);

}
