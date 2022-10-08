package com.leadpet.www.infrastructure.db.posts.donationPost;

import com.leadpet.www.infrastructure.db.posts.donationPost.condition.SearchDonationPostCondition;
import com.leadpet.www.infrastructure.domain.donation.DonationMethod;
import com.leadpet.www.presentation.dto.response.post.donation.DonationPostPageResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.leadpet.www.infrastructure.domain.posts.QDonationPosts.donationPosts;

/**
 * DonationPostsRepositoryImpl
 */
@lombok.RequiredArgsConstructor
public class DonationPostsRepositoryImpl implements DonationPostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<DonationPostPageResponseDto> searchAll(@NonNull final SearchDonationPostCondition condition, final Pageable pageable) {
        List<DonationPostPageResponseDto> content = queryFactory
                .select(Projections.constructor(
                        DonationPostPageResponseDto.class,
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
                .where(
                        betweenStartDateAndEndDate(),
                        eqUserIdWith(condition.getUserId()),
                        eqDonationMethodWith(condition.getDonationMethod())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(donationPosts.donationPostId.count())
                .from(donationPosts)
                .where(
                        betweenStartDateAndEndDate(),
                        eqUserIdWith(condition.getUserId()),
                        eqDonationMethodWith(condition.getDonationMethod())
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression betweenStartDateAndEndDate() {
        final LocalDateTime now = LocalDateTime.now();
        return startDateLoe(now).and(endDateGoe(now));
    }

    private BooleanExpression startDateLoe(final LocalDateTime targetTime) {
        return donationPosts.startDate.loe(targetTime);
    }

    private BooleanExpression endDateGoe(final LocalDateTime targetTime) {
        return donationPosts.endDate.goe(targetTime);
    }

    private BooleanExpression eqUserIdWith(final String userId) {
        return StringUtils.isBlank(userId) ? null : donationPosts.user.userId.eq(userId);
    }

    private BooleanExpression eqDonationMethodWith(final DonationMethod donationMethod) {
        return Objects.isNull(donationMethod) ? null : donationPosts.donationMethod.eq(donationMethod);
    }
}
