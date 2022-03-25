package com.leadpet.www.infrastructure.db;

import com.leadpet.www.infrastructure.domain.posts.NormalPosts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * NormalPostsRepository
 */
@Repository
public interface NormalPostsRepository extends JpaRepository<NormalPosts, String> {
}
