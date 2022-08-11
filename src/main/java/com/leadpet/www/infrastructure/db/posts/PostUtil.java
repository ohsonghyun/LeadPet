package com.leadpet.www.infrastructure.db.posts;

import com.leadpet.www.infrastructure.db.posts.adoptionPost.AdoptionPostsRepository;
import com.leadpet.www.infrastructure.db.posts.donationPost.DonationPostsRepository;
import com.leadpet.www.infrastructure.db.posts.normalPost.NormalPostsRepository;
import com.leadpet.www.infrastructure.domain.posts.PostType;
import com.leadpet.www.infrastructure.exception.UnexpectedOperationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * PostUtil
 * <p>독립된 도메인으로 관리되고 있는 *PostsRepository를 공통으로 다룰 수 있는 utility class</p>
 */
@Component
@lombok.RequiredArgsConstructor
public class PostUtil {

    private final NormalPostsRepository normalPostsRepository;
    private final DonationPostsRepository donationPostsRepository;
    private final AdoptionPostsRepository adoptionPostsRepository;

    /**
     * 피드타입을 통해서 해당하는 Repository를 취득
     *
     * @param postType {@code PostType} 취득하고자 하는 피드타입
     * @return {@code JpaRepository}
     */
    @NonNull
    public JpaRepository<?, String> getRepositoryBy(@NonNull final PostType postType) {
        switch (postType) {
            case NORMAL_POST:
                return normalPostsRepository;
            case ADOPTION_POST:
                return adoptionPostsRepository;
            case DONATION_POST:
                return donationPostsRepository;
            default:
                throw new UnexpectedOperationException();
        }
    }
}
