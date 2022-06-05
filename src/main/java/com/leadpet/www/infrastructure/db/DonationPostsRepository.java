package com.leadpet.www.infrastructure.db;

import com.leadpet.www.infrastructure.domain.posts.DonationPosts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * DonationPostsRepository
 */
@Repository
public interface DonationPostsRepository extends JpaRepository<DonationPosts, String> {
}
