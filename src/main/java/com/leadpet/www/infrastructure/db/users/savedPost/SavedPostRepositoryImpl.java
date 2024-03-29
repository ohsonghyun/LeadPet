package com.leadpet.www.infrastructure.db.users.savedPost;

import com.leadpet.www.infrastructure.domain.posts.PostType;
import com.leadpet.www.presentation.dto.response.post.adoption.SimpleAdoptionPostResponse;
import com.leadpet.www.presentation.dto.response.post.donation.SimpleDonationPostResponse;
import com.leadpet.www.presentation.dto.response.post.normal.SimpleNormalPostResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

import java.util.List;

import static com.leadpet.www.infrastructure.domain.posts.QAdoptionPosts.adoptionPosts;
import static com.leadpet.www.infrastructure.domain.posts.QDonationPosts.donationPosts;
import static com.leadpet.www.infrastructure.domain.posts.QNormalPosts.normalPosts;
import static com.leadpet.www.infrastructure.domain.users.savedPost.QSavedPost.savedPost;

/**
 * SavedPostRepositoryImpl
 */
@RequiredArgsConstructor
public class SavedPostRepositoryImpl implements SavedPostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<SimpleNormalPostResponse> findSavedNormalPostsByUserId(final String userId, final Pageable pageable) {
        List<SimpleNormalPostResponse> content = queryFactory.select(
                        Projections.constructor(
                                SimpleNormalPostResponse.class,
                                normalPosts.normalPostId,
                                normalPosts.title,
                                normalPosts.contents,
                                normalPosts.images,
                                normalPosts.createdDate,
                                normalPosts.user.userId,
                                normalPosts.user.name,
                                normalPosts.user.profileImage
                        ))
                .from(normalPosts)
                .leftJoin(savedPost).on(normalPosts.normalPostId.eq(savedPost.postId))
                .where(eqSavedPostUserIdWith(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory.select(savedPost.postId.count())
                .from(savedPost)
                .where(
                        eqSavedPostUserIdWith(userId),
                        savedPost.postType.eq(PostType.NORMAL_POST)
                )
                .fetchOne();
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<SimpleAdoptionPostResponse> findSavedAdoptionPostsByUserId(String userId, Pageable pageable) {
        List<SimpleAdoptionPostResponse> content = queryFactory.select(
                        Projections.constructor(
                                SimpleAdoptionPostResponse.class,
                                adoptionPosts.adoptionPostId,
                                adoptionPosts.startDate,
                                adoptionPosts.endDate,
                                adoptionPosts.euthanasiaDate,
                                adoptionPosts.title,
                                adoptionPosts.contents,
                                adoptionPosts.animalType,
                                adoptionPosts.breed,
                                adoptionPosts.gender,
                                adoptionPosts.neutering,
                                adoptionPosts.age,
                                adoptionPosts.disease,
                                adoptionPosts.images,
                                adoptionPosts.user.userId.as("userId"),
                                adoptionPosts.user.name.as("userName"),
                                adoptionPosts.user.profileImage.as("profileImage")
                        ))
                .from(adoptionPosts)
                .leftJoin(savedPost).on(adoptionPosts.adoptionPostId.eq(savedPost.postId))
                .where(eqSavedPostUserIdWith(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory.select(savedPost.postId.count())
                .from(savedPost)
                .where(
                        eqSavedPostUserIdWith(userId),
                        savedPost.postType.eq(PostType.ADOPTION_POST)
                )
                .fetchOne();
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<SimpleDonationPostResponse> findSavedDonationPostsByUserId(String userId, Pageable pageable) {
        List<SimpleDonationPostResponse> content = queryFactory.select(
                        Projections.constructor(
                                SimpleDonationPostResponse.class,
                                donationPosts.donationPostId,
                                donationPosts.startDate,
                                donationPosts.endDate,
                                donationPosts.title,
                                donationPosts.donationMethod,
                                donationPosts.contents,
                                donationPosts.images,
                                donationPosts.user.userId.as("userId"),
                                donationPosts.user.name.as("userName"),
                                donationPosts.user.profileImage.as("profileImage")
                        ))
                .from(donationPosts)
                .leftJoin(savedPost).on(donationPosts.donationPostId.eq(savedPost.postId))
                .where(eqSavedPostUserIdWith(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory.select(savedPost.postId.count())
                .from(savedPost)
                .where(
                        eqSavedPostUserIdWith(userId),
                        savedPost.postType.eq(PostType.DONATION_POST)
                )
                .fetchOne();
        return new PageImpl<>(content, pageable, total);
    }

    @Nullable
    private BooleanExpression eqSavedPostUserIdWith(final String userId) {
        return StringUtils.isBlank(userId) ? null : savedPost.user.userId.eq(userId);
    }

}
