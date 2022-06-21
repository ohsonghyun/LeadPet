package com.leadpet.www.infrastructure.db;

import com.leadpet.www.infrastructure.domain.posts.AdoptionPosts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * AdoptionPostsRepository
 */
@Repository
public interface AdoptionPostsRepository extends JpaRepository<AdoptionPosts, String>, AdoptionPostRepositoryCustom {
}
