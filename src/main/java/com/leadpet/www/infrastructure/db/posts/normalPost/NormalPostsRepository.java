package com.leadpet.www.infrastructure.db.posts.normalPost;

import com.leadpet.www.infrastructure.domain.posts.NormalPosts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * NormalPostsRepository
 */
@Repository
public interface NormalPostsRepository extends JpaRepository<NormalPosts, String>, NormalPostsRepositoryCustom {
}
