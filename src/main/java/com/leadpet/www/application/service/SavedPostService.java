package com.leadpet.www.application.service;

import com.leadpet.www.infrastructure.db.posts.PostUtil;
import com.leadpet.www.infrastructure.db.users.UsersRepository;
import com.leadpet.www.infrastructure.db.users.savedPost.SavedPostRepository;
import com.leadpet.www.infrastructure.domain.posts.PostType;
import com.leadpet.www.infrastructure.domain.users.Users;
import com.leadpet.www.infrastructure.domain.users.savedPost.SavedPost;
import com.leadpet.www.infrastructure.exception.PostNotFoundException;
import com.leadpet.www.infrastructure.exception.SavedPostNotFoundException;
import com.leadpet.www.infrastructure.exception.UnauthorizedUserException;
import com.leadpet.www.infrastructure.exception.login.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * SavedPostService
 */
@Service
@Slf4j
@Transactional(readOnly = true)
@lombok.RequiredArgsConstructor
public class SavedPostService {

    private final UsersRepository usersRepository;
    private final SavedPostRepository savedPostRepository;
    private final PostUtil postUtil;

    /**
     * 새로운 저장피드 추가
     *
     * @param postId   {@code String}
     * @param postType {@code PostType}
     * @param userId   {@code String}
     * @return {@code SavedPost} 저장한 저장피드
     */
    @Transactional
    public SavedPost save(
            @NonNull final String postId,
            @NonNull final PostType postType,
            @NonNull final String userId
    ) {
        Users user = usersRepository.findByUserId(userId);
        if (Objects.isNull(user)) {
            log.warn("[SavedPostService#save] 존재하지 않는 유저ID: {}", userId);
            throw new UserNotFoundException("[저장피드] 존재하지 않는 유저ID");
        }

        postUtil.getRepositoryBy(postType)
                .findById(postId)
                .orElseThrow(() -> {
                    log.warn("[SavedPostService#save] 존재하지 않는 피드ID: {}", postId);
                    return new PostNotFoundException("[저장피드] 존재하지 않는 피드ID");
                });

        return savedPostRepository.save(
                SavedPost.builder()
                        .savedPostId(String.format("SP_%s", RandomStringUtils.random(10, true, true)))
                        .postId(postId)
                        .postType(postType)
                        .user(user)
                        .build()
        );
    }

    /**
     * 저장피드 삭제
     *
     * @param savedPostId {@code String} 삭제 대상 저장피드ID
     * @return {@code String} 삭제 성공한 저장피드ID
     */
    @Transactional
    public String deleteById(final String userId, final String savedPostId) {
        Users user = usersRepository.findByUserId(userId);
        if (Objects.isNull(user)) {
            log.warn("[SavedPostService#deleteById] 존재하지 않는 유저ID: {}", userId);
            throw new UserNotFoundException("[저장피드] 존재하지 않는 유저ID");
        }

        SavedPost savedPost = savedPostRepository.findById(savedPostId).orElseThrow(() -> {
            log.warn("[SavedPostService#deleteById] 존재하지 않는 저장피드ID: {}", savedPostId);
            return new SavedPostNotFoundException("[저장피드] 존재하지 않는 저장피드ID");
        });

        if (!savedPost.getUser().isSameUser(userId)) {
            log.error("[SavedPostService#deleteById] 권한 없는 유저: {}", userId);
            throw new UnauthorizedUserException("[저장피드] 권한 없는 유저");
        }
        savedPostRepository.deleteById(savedPostId);
        return savedPostId;
    }
}
