package com.leadpet.www.infrastructure.db.users;

import com.leadpet.www.infrastructure.db.users.condition.SearchShelterCondition;
import com.leadpet.www.infrastructure.domain.users.UserType;
import com.leadpet.www.infrastructure.domain.users.Users;
import com.leadpet.www.presentation.dto.response.user.ShelterPageResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static com.leadpet.www.infrastructure.domain.posts.QAdoptionPosts.adoptionPosts;
import static com.leadpet.www.infrastructure.domain.posts.QDonationPosts.donationPosts;
import static com.leadpet.www.infrastructure.domain.posts.QNormalPosts.normalPosts;
import static com.leadpet.www.infrastructure.domain.users.QUsers.users;

/**
 * UsersRepositoryImpl
 */
@lombok.RequiredArgsConstructor
public class UsersRepositoryImpl implements UsersRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ShelterPageResponseDto> searchShelters(SearchShelterCondition condition, Pageable pageable) {
        // 보호소 이름
        List<Users> shelters = queryFactory
                .select(users)
                .from(users)
                .where(users.userType.eq(UserType.SHELTER))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<ShelterPageResponseDto> shelterPageResponseDto = new ArrayList<>();

        // 총 게시글 수
        for (Users shelter : shelters) {
            Long normalPostCount = queryFactory
                    .select(normalPosts.count())
                    .from(normalPosts)
                    .where(normalPosts.user.userId.eq(shelter.getUserId()))
                    .fetchOne();

            Long donationPostCount = queryFactory
                    .select(donationPosts.count())
                    .from(donationPosts)
                    .where(donationPosts.user.userId.eq(shelter.getUserId()))
                    .fetchOne();

            Long adoptionPostCount = queryFactory
                    .select(adoptionPosts.count())
                    .from(adoptionPosts)
                    .where(adoptionPosts.user.userId.eq(shelter.getUserId()))
                    .fetchOne();

            shelterPageResponseDto.add(
                    ShelterPageResponseDto.builder()
                            .shelterName(shelter.getShelterName())
                            .allFeedCount(normalPostCount + donationPostCount + adoptionPostCount)
                            .assessmentStatus(shelter.getShelterAssessmentStatus())
                            .build()
            );
        }

        // 총 카운트
        final Long total = queryFactory
                .select(users.userId.count())
                .from(users)
                .where(users.userType.eq(UserType.SHELTER))
                .fetchOne();

        return new PageImpl<>(shelterPageResponseDto, pageable, total);
    }
}
