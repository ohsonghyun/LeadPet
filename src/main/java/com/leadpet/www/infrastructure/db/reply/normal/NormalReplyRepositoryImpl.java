package com.leadpet.www.infrastructure.db.reply.normal;

import com.leadpet.www.presentation.dto.response.reply.normal.NormalReplyPageResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.leadpet.www.infrastructure.domain.reply.normal.QNormalReply.normalReply;

/**
 * NormalReplyRepositoryImpl
 */
@lombok.RequiredArgsConstructor
public class NormalReplyRepositoryImpl implements NormalReplyRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<NormalReplyPageResponseDto> findByPostId(final String postId, final Pageable pageable) {
        List<NormalReplyPageResponseDto> content = queryFactory
                .select(
                        Projections.constructor(
                                NormalReplyPageResponseDto.class,
                                normalReply.normalReplyId,
                                normalReply.user.userId,
                                normalReply.user.name,
                                normalReply.content,
                                normalReply.createdDate
                        )
                )
                .from(normalReply)
                .where(eqNormalPostIdWith(postId))
                .orderBy(normalReply.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(normalReply.normalReplyId.count())
                .from(normalReply)
                .where(eqNormalPostIdWith(postId))
                .fetchOne();
        return new PageImpl<>(content, pageable, total);
    }

    /**
     * 일상피드ID 조건
     *
     * @param normalPostId {@code String}
     * @return {@code BooleanExpression}
     */
    private BooleanExpression eqNormalPostIdWith(final String normalPostId) {
        return StringUtils.isBlank(normalPostId) ? null : normalReply.normalPost.normalPostId.eq(normalPostId);
    }
}
