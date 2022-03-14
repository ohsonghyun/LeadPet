package com.leadpet.www.infrastructure.db;

import com.leadpet.www.infrastructure.domain.posts.NormalPosts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * NormalPostsRepository
 */
@Repository
public interface NormalPostsRepository extends JpaRepository<NormalPosts, Long> {

    /**
     * NormalPostId와 UserId로 일반 게시글 취득
     *
     * @param normalPostId 게시글 아이디
     * @param UserId 유저 아이디
     * @return {@NormalPosts}
     */
    NormalPosts findByNormalPostIdAndUserId(Long normalPostId, String UserId);
}
