package com.leadpet.www.infrastructure.db.posts.normalPost;

import com.leadpet.www.infrastructure.db.posts.normalPost.condition.SearchNormalPostCondition;
import com.leadpet.www.presentation.dto.response.post.NormalPostResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

import java.util.List;

import static com.leadpet.www.infrastructure.domain.liked.QLiked.liked;
import static com.leadpet.www.infrastructure.domain.posts.QNormalPosts.normalPosts;

/**
 * NormalPostsRepositoryImpl
 */
@lombok.RequiredArgsConstructor
public class NormalPostsRepositoryImpl implements NormalPostsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<NormalPostResponse> searchAll(@NonNull final SearchNormalPostCondition condition, final Pageable pageable) {
        final List<NormalPostResponse> content = queryFactory
                .select(
                        Projections.constructor(
                                NormalPostResponse.class,
                                normalPosts.normalPostId,
                                normalPosts.title,
                                normalPosts.contents,
                                normalPosts.images,
                                normalPosts.createdDate,
                                liked.postId.count().as("likedCount"),
                                normalPosts.user.userId.as("userId")
                        )
                )
                .from(normalPosts)
                .leftJoin(liked).on(liked.postId.eq(normalPosts.normalPostId))
                .where(eqUserIdWith(condition.getUserId()))
                .groupBy(normalPosts.normalPostId)
                .orderBy(normalPosts.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        final Long total = queryFactory
                .select(normalPosts.normalPostId.count())
                .from(normalPosts)
                .where(eqUserIdWith(condition.getUserId()))
                .fetchOne();
        return new PageImpl<>(content, pageable, total);
    }

    /**
     * 특정 UserId 조건
     *
     * @param userId {@code String}
     * @return {@code BooleanExpression}
     */
    private BooleanExpression eqUserIdWith(final String userId) {
        return StringUtils.isBlank(userId) ? null : normalPosts.user.userId.eq(userId);
    }
}
