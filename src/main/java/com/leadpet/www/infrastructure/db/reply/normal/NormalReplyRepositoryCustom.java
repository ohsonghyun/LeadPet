package com.leadpet.www.infrastructure.db.reply.normal;

import com.leadpet.www.presentation.dto.response.reply.normal.NormalReplyPageResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * NormalReplyRepositoryCustom
 */
public interface NormalReplyRepositoryCustom {

    /**
     * 일상피드ID를 통해 관련 댓글 취득
     *
     * @param postId {@code String} 일상피드ID
     * @return {@code Page<NormalReplyPageResponseDto>}
     */
    Page<NormalReplyPageResponseDto> findByPostId(final String postId, final Pageable pageable);
}
