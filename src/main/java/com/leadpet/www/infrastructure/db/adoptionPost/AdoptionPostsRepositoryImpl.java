package com.leadpet.www.infrastructure.db.adoptionPost;

import com.leadpet.www.infrastructure.db.adoptionPost.condition.SearchAdoptionPostCondition;
import com.leadpet.www.presentation.dto.response.post.adoption.AdoptionPostPageResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

import java.util.List;

import static com.leadpet.www.infrastructure.domain.posts.QAdoptionPosts.adoptionPosts;

/**
 * AdoptionPostsRepositoryImpl
 */
@lombok.RequiredArgsConstructor
public class AdoptionPostsRepositoryImpl implements AdoptionPostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<AdoptionPostPageResponseDto> searchAll(@NonNull final SearchAdoptionPostCondition condition, final Pageable pageable) {
        List<AdoptionPostPageResponseDto> content = queryFactory
                .select(
                        Projections.constructor(
                                AdoptionPostPageResponseDto.class,
                                adoptionPosts.adoptionPostId,
                                adoptionPosts.startDate,
                                adoptionPosts.endDate,
                                adoptionPosts.euthanasiaDate,
                                adoptionPosts.title,
                                adoptionPosts.contents,
                                adoptionPosts.animalType,
                                adoptionPosts.species,
                                adoptionPosts.gender,
                                adoptionPosts.neutering,
                                adoptionPosts.age,
                                adoptionPosts.disease,
                                adoptionPosts.images,
                                adoptionPosts.user.userId.as("userId")
                        ))
                .from(adoptionPosts)
                .where(eqUserIdWith(condition.getUserId()))
                .orderBy(adoptionPosts.euthanasiaDate.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(adoptionPosts.count())
                .from(adoptionPosts)
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
        return StringUtils.isBlank(userId) ? null : adoptionPosts.user.userId.eq(userId);
    }
}
