package com.leadpet.www.infrastructure.db.normalPost;

import com.leadpet.www.infrastructure.domain.posts.NormalPosts;
import com.leadpet.www.infrastructure.domain.posts.QNormalPosts;
import com.leadpet.www.presentation.dto.response.post.NormalPostResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.leadpet.www.infrastructure.domain.posts.QNormalPosts.normalPosts;

/**
 * NormalPostsRepositoryImpl
 */
@lombok.RequiredArgsConstructor
public class NormalPostsRepositoryImpl implements NormalPostsRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public Page<NormalPostResponse> searchAll(Pageable pageable) {
        final List<NormalPostResponse> content = queryFactory
                .select(
                        Projections.constructor(
                                NormalPostResponse.class,
                                normalPosts.normalPostId,
                                normalPosts.title,
                                normalPosts.contents,
                                normalPosts.images,
                                normalPosts.user.userId.as("userId")
                        )
                )
                .from(normalPosts)
                .orderBy(normalPosts.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        final Long total = queryFactory
                .select(normalPosts.normalPostId.count())
                .from(normalPosts)
                .fetchOne();
        return new PageImpl<>(content, pageable, total);
    }
}
