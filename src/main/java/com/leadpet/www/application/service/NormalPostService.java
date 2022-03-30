package com.leadpet.www.application.service;

import com.leadpet.www.infrastructure.db.NormalPostsRepository;
import com.leadpet.www.infrastructure.db.UsersRepository;
import com.leadpet.www.infrastructure.domain.posts.NormalPosts;
import com.leadpet.www.infrastructure.domain.users.Users;
import com.leadpet.www.infrastructure.exception.UnauthorizedUserException;
import com.leadpet.www.infrastructure.exception.PostNotFoundException;
import com.leadpet.www.infrastructure.exception.WrongArgumentsException;
import com.leadpet.www.infrastructure.exception.login.UserNotFoundException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * NormalPostService
 */
@Service
@lombok.RequiredArgsConstructor
public class NormalPostService {

    private final UsersRepository usersRepository;
    private final NormalPostsRepository normalPostsRepository;

    /**
     * 페이징으로 지정한 일반 게시물 취득
     *
     * @return {@code List<NormalPosts>}
     */
    public List<NormalPosts> getNormalPostsWith(final int page, final int size) {
        if (checkNegative.test(page) || !checkPositive.test(size)) {
            throw new WrongArgumentsException("Error: 옳지 않은 파라미터");
        }
        return normalPostsRepository.findAll(PageRequest.of(page, size, Sort.by("createdDate"))).getContent();
    }

    /**
     * 양의 정수인지 확인
     */
    private Predicate<Integer> checkPositive = num -> num > 0;

    /**
     * 음의 정수인지 확인
     */
    private Predicate<Integer> checkNegative = num -> num < 0;

    /**
     * 신규 일반 게시물 등록
     *
     * @param newNormalPost 신규 일반 게시물
     * @param userId {@code String}
     * @return {@code NormalPosts}
     */
    public NormalPosts addNewPost(@NonNull final NormalPosts newNormalPost, @NonNull final String userId) {
        // 존재하는 유저인지 확인
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Error: 존재하지 않는 유저"));
        return normalPostsRepository.save(
                NormalPosts.builder()
                        .normalPostId(String.format("NP_%s", RandomStringUtils.random(10, true, true)))
                        .title(newNormalPost.getTitle())
                        .contents(newNormalPost.getContents())
                        .images(newNormalPost.getImages())
                        .tags(newNormalPost.getTags())
                        .user(user)
                        .build());
    }

    /**
     * 일반 게시물 수정
     *
     * @param updatingNormalPost 수정할 일반 게시물
     * @param userId {@code String}
     * @return {@code NormalPosts}
     */
    @Transactional
    public NormalPosts updateNormalPost(@NonNull final NormalPosts updatingNormalPost, @NonNull final String userId) {
        NormalPosts targetPost = this.normalPostsRepository.findById(updatingNormalPost.getNormalPostId())
                .orElseThrow(() -> new PostNotFoundException());

        // 권한이 없는 유저는 게시물 획득 불가
        Users author = targetPost.getUser();
        if (Objects.isNull(author) || ObjectUtils.notEqual(author.getUserId(), userId)) {
            throw new UnauthorizedUserException();
        }

        NormalPosts updatedPost = targetPost.update(updatingNormalPost);
        return updatedPost;
    }

    /**
     * 일반 게시글 삭제
     *
     * @param normalPostId
     * @param userId {@code String}
     * @return {@code String} 삭제한 일반 게시글 ID
     */
    public String deleteNormalPost(@NonNull final String normalPostId, @NonNull final String userId) {
        NormalPosts targetPost = this.normalPostsRepository.findById(normalPostId)
                .orElseThrow(() -> new PostNotFoundException());
        if (ObjectUtils.notEqual(userId, targetPost.getUser().getUserId())) {
            throw new UnauthorizedUserException();
        }
        this.normalPostsRepository.deleteById(normalPostId);
        return normalPostId;
    }
}
