package com.leadpet.www.infrastructure.db;

import com.leadpet.www.infrastructure.domain.posts.AdoptionPostRepositoryCustom;
import com.leadpet.www.presentation.dto.response.post.adoption.AdoptionPostPageResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.leadpet.www.infrastructure.domain.posts.QAdoptionPosts.adoptionPosts;

/**
 * AdoptionPostsRepositoryImpl
 */
@lombok.RequiredArgsConstructor
public class AdoptionPostsRepositoryImpl implements AdoptionPostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<AdoptionPostPageResponseDto> searchAll(Pageable pageable) {
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
                                adoptionPosts.images,
                                adoptionPosts.user.userId.as("userId")
                        ))
                .from(adoptionPosts)
                .orderBy(adoptionPosts.euthanasiaDate.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(adoptionPosts.count())
                .from(adoptionPosts)
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }
}
