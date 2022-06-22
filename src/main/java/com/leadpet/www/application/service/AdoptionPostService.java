package com.leadpet.www.application.service;

import com.leadpet.www.infrastructure.db.adoptionPost.AdoptionPostsRepository;
import com.leadpet.www.infrastructure.db.users.UsersRepository;
import com.leadpet.www.infrastructure.domain.posts.AdoptionPosts;
import com.leadpet.www.infrastructure.domain.users.Users;
import com.leadpet.www.infrastructure.exception.login.UserNotFoundException;
import com.leadpet.www.presentation.dto.response.post.adoption.AdoptionPostPageResponseDto;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * AdoptionPostService
 */
@Service
@lombok.RequiredArgsConstructor
public class AdoptionPostService {

    private final AdoptionPostsRepository adoptionPostsRepository;
    private final UsersRepository usersRepository;

    /**
     * 새로운 임양 피드를 추가
     *
     * @param newAdoptionPost 저장할 기부 피드
     * @param userId          기부 피드를 등록한 유저ID
     * @return {@code DonationPosts} 저장된 기부 피드
     */
    public AdoptionPosts addNewPost(final AdoptionPosts newAdoptionPost, final String userId) {
        // 실제 유저가 존재하는지 확인
        Users user = usersRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        // 피드 아이디 & 유저 추가
        AdoptionPosts adoptionPostWithUser = AdoptionPosts.builder()
                .adoptionPostId(String.format("AP_%s", RandomStringUtils.random(10, true, true)))
                .startDate(newAdoptionPost.getStartDate())
                .endDate(newAdoptionPost.getEndDate())
                .euthanasiaDate(newAdoptionPost.getEuthanasiaDate())
                .title(newAdoptionPost.getTitle())
                .contents(newAdoptionPost.getContents())
                .images(newAdoptionPost.getImages())
                .animalType(newAdoptionPost.getAnimalType())
                .species(newAdoptionPost.getSpecies())
                .gender(newAdoptionPost.getGender())
                .neutering(newAdoptionPost.getNeutering())
                .user(user)
                .build();

        // 새로운 도네이션 피드를 저장
        return adoptionPostsRepository.save(adoptionPostWithUser);
    }

    /**
     * 입양 피드 리스트를 반환 (페이지네이션 포함)
     *
     * @param pageable
     * @return {@code Page<AdoptionPostPageResponseDto>}
     */
    public Page<AdoptionPostPageResponseDto> searchAll(Pageable pageable) {
        return adoptionPostsRepository.searchAll(pageable);
    }
}
