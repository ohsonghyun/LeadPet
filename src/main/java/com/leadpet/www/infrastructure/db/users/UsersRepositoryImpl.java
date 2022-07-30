package com.leadpet.www.infrastructure.db.users;

import com.leadpet.www.infrastructure.db.users.condition.SearchShelterCondition;
import com.leadpet.www.infrastructure.domain.users.UserType;
import com.leadpet.www.infrastructure.domain.users.Users;
import com.leadpet.www.presentation.dto.response.user.ShelterPageResponseDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

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
                .where(
                        eqUserTypeShelter(),
                        containsCityName(condition.getCityName()),
                        containsShelterName(condition.getShelterName())
                )
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
                            .userId(shelter.getUserId())
                            .shelterName(shelter.getShelterName())
                            .allFeedCount(normalPostCount + donationPostCount + adoptionPostCount)
                            .assessmentStatus(shelter.getShelterAssessmentStatus())
                            .profileImage(shelter.getProfileImage())
                            .build()
            );
        }

        // 총 카운트
        final Long total = queryFactory
                .select(users.userId.count())
                .from(users)
                .where(
                        eqUserTypeShelter(),
                        containsCityName(condition.getCityName()),
                        containsShelterName(condition.getShelterName())
                )
                .fetchOne();

        return new PageImpl<>(shelterPageResponseDto, pageable, total);
    }

    /**
     * 보호소 주소 조건 추가
     *
     * @param cityName
     * @return {@code BooleanExpression}
     */
    @Nullable
    private BooleanExpression containsCityName(final String cityName) {
        return StringUtils.isBlank(cityName) ? null : users.shelterAddress.contains(cityName);
    }

    /**
     * 보호소 이름 조건 추가
     *
     * @param shelterName
     * @return {@code BooleanExpression}
     */
    @Nullable
    private BooleanExpression containsShelterName(final String shelterName) {
        return StringUtils.isBlank(shelterName) ? null : users.shelterName.contains(shelterName);
    }

    @Nullable
    private BooleanExpression eqUserTypeShelter() {
        return users.userType.eq(UserType.SHELTER);
    }

    @Nullable
    @Override
    public Users findShelterByUserId(final String userId) {
        return queryFactory
                .selectFrom(users)
                .where(
                        users.userId.eq(userId),
                        eqUserTypeShelter()
                )
                .fetchOne();
    }
}
