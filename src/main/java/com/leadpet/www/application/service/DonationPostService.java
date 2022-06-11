package com.leadpet.www.application.service;

import com.leadpet.www.infrastructure.db.DonationPostsRepository;
import com.leadpet.www.infrastructure.db.UsersRepository;
import com.leadpet.www.infrastructure.domain.posts.DonationPosts;
import com.leadpet.www.infrastructure.domain.users.Users;
import com.leadpet.www.infrastructure.exception.login.UserNotFoundException;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * DonationPostService
 */
@lombok.RequiredArgsConstructor
public class DonationPostService {

    private final DonationPostsRepository donationPostsRepository;
    private final UsersRepository usersRepository;

    /**
     * 새로운 기부 피드를 추가
     *
     * @param newDonationPost 저장할 기부 피드
     * @param userId          기부 피드를 등록한 유저ID
     * @return {@code DonationPosts} 저장된 기부 피드
     */
    public DonationPosts addNewPost(final DonationPosts newDonationPost, final String userId) {
        // 실제 유저가 존재하는지 확인
        Users user = usersRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        // 피드 아이디 & 유저 추가
        DonationPosts donationPostWithUser = DonationPosts.builder()
                .donationPostId(String.format("DP_%s", RandomStringUtils.random(10, true, true)))
                .startDate(newDonationPost.getStartDate())
                .endDate(newDonationPost.getEndDate())
                .title(newDonationPost.getTitle())
                .donationMethod(newDonationPost.getDonationMethod())
                .contents(newDonationPost.getContents())
                .images(newDonationPost.getImages())
                .user(user)
                .build();

        // 존재하면 새로운 도네이션 피드를 저장
        return donationPostsRepository.save(donationPostWithUser);
    }
}
