package com.leadpet.www.infrastructure.db.posts.normalPost;

import com.leadpet.www.infrastructure.db.posts.normalPost.condition.SearchNormalPostCondition;
import com.leadpet.www.infrastructure.domain.posts.NormalPosts;
import com.leadpet.www.presentation.dto.response.post.NormalPostResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Null;
import java.util.List;

import static com.leadpet.www.infrastructure.domain.liked.QLiked.liked;
import static com.leadpet.www.infrastructure.domain.posts.QNormalPosts.normalPosts;
import static com.leadpet.www.infrastructure.domain.reply.normal.QNormalReply.normalReply;

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
                                eqLikedUserIdWith(condition.getLikedUserId()).as("liked"),
                                liked.postId.count().as("likedCount"),
                                normalReply.normalPost.normalPostId.count().as("commentCount"),
                                normalPosts.user.userId.as("userId"),
                                normalPosts.user.name.as("userName"),
                                normalPosts.user.profileImage.as("profileImage")
                        )
                )
                .distinct()
                .from(normalPosts)
                .leftJoin(liked).on(liked.postId.eq(normalPosts.normalPostId))
                .leftJoin(normalReply).on(normalReply.normalPost.normalPostId.eq(normalPosts.normalPostId))
                .where(eqUserIdWith(condition.getUserId()))
                .groupBy(normalPosts.normalPostId, liked.userId)
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

    @Nullable
    @Override
    public NormalPosts selectNormalPost(final String postId) {
        return queryFactory
                .selectFrom(normalPosts)
                .where(
                        eqPostIdWith(postId)
                )
                .fetchOne();
    }

    /**
     * 특정 UserId 조건
     *
     * @param userId {@code String}
     * @return {@code BooleanExpression}
     */
    private BooleanExpression eqUserIdWith(@Nullable final String userId) {
        return StringUtils.isBlank(userId) ? null : normalPosts.user.userId.eq(userId);
    }

    /**
     * 특정 PostId 조건
     *
     * @param postId
     * @return
     */
    private BooleanExpression eqPostIdWith(final String postId) {
        return StringUtils.isBlank(postId) ? null : normalPosts.normalPostId.eq(postId);
    }

    /**
     * 피드에 대해 좋아요를 클릭한 userId 조건
     * <p>20220813 프로젝션에 사용</p>
     *
     * @param likedUserId {@code String} 좋아요 클릭한 userId
     * @return {@code BooleanExpression} likedUserId가 null 이면 false, 아니면 조건 반환
     */
    private BooleanExpression eqLikedUserIdWith(@Nullable final String likedUserId) {
        return StringUtils.isBlank(likedUserId) ? Expressions.asBoolean(false) : liked.userId.eq(likedUserId);
    }
}
