package com.leadpet.www.infrastructure.db.donationPost;

import com.leadpet.www.presentation.dto.response.post.donation.DonationPostPageResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static com.leadpet.www.infrastructure.domain.posts.QDonationPosts.donationPosts;

/**
 * DonationPostsRepositoryImpl
 */
@lombok.RequiredArgsConstructor
public class DonationPostsRepositoryImpl implements DonationPostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<DonationPostPageResponseDto> searchAll(Pageable pageable) {
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
                        donationPosts.user.userId.as("userId")
                ))
                .from(donationPosts)
                .where(
                        betweenStartDateAndEndDate()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(donationPosts.donationPostId.count())
                .from(donationPosts)
                .where(
                        betweenStartDateAndEndDate()
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
}
