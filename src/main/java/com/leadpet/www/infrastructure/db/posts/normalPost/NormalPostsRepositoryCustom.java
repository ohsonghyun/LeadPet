package com.leadpet.www.infrastructure.db.posts.normalPost;

import com.leadpet.www.infrastructure.db.posts.normalPost.condition.SearchNormalPostCondition;
import com.leadpet.www.infrastructure.domain.posts.NormalPosts;
import com.leadpet.www.presentation.dto.response.post.NormalPostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

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

    /**
     * 게시글 상세조회 (postId)
     *
     * @param postId {@code postId}
     * @return {@code NormalPosts}
     */
    @Nullable
    NormalPosts selectNormalPost(String postId);
}
