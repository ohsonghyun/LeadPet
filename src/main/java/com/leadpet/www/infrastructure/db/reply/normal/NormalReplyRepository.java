package com.leadpet.www.infrastructure.db.reply.normal;

import com.leadpet.www.infrastructure.domain.reply.normal.NormalReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * NormalReplyRepository
 */
@Repository
public interface NormalReplyRepository extends JpaRepository<NormalReply, String>, NormalReplyRepositoryCustom {
}
