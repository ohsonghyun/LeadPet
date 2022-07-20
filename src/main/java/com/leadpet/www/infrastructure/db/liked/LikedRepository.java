package com.leadpet.www.infrastructure.db.liked;

import com.leadpet.www.infrastructure.domain.liked.Liked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;

/**
 * LikedRepository
 */
public interface LikedRepository extends JpaRepository<Liked, String> {
    /**
     * UserId, PostId 로 좋아요 데이터 취득
     *
     * @param userId {@code String}
     * @param postId {@code String}
     * @return {@code Liked}
     */
    @Nullable
    Liked findByUserIdAndPostId(final String userId, final String postId);

    /**
     * UserId, PostId 로 좋아요 데이터 삭제
     *
     * @param userId {@code String}
     * @param postId {@code String}
     */
    void deleteByUserIdAndPostId(final String userId, final String postId);
}
