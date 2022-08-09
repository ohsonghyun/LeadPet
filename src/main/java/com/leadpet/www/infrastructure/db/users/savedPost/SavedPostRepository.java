package com.leadpet.www.infrastructure.db.users.savedPost;

import com.leadpet.www.infrastructure.domain.users.SavedPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * SavedPostRepository
 */
@Repository
public interface SavedPostRepository extends JpaRepository<SavedPost, String> {
}
