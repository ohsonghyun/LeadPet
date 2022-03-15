package com.leadpet.www.application.service;

import com.leadpet.www.infrastructure.db.NormalPostsRepository;
import com.leadpet.www.infrastructure.db.UsersRepository;
import com.leadpet.www.infrastructure.domain.posts.NormalPosts;
import com.leadpet.www.infrastructure.domain.users.LoginMethod;
import com.leadpet.www.infrastructure.domain.users.Users;
import com.leadpet.www.infrastructure.exception.PostNotFoundException;
import com.leadpet.www.infrastructure.exception.login.UserNotFoundException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * NormalPostService
 */
@Service
@lombok.RequiredArgsConstructor
public class NormalPostService {

    private final UsersRepository usersRepository;
    private final NormalPostsRepository normalPostsRepository;

    /**
     * 모든 일반 게시물 취득
     *
     * @return {@code List<NormalPosts>}
     */
    public List<NormalPosts> getAllNormalPosts() {
        return normalPostsRepository.findAll();
    }

    /**
     * 신규 일반 게시물 등록
     *
     * @param newNormalPost 신규 일반 게시물
     * @return {@code NormalPosts}
     */
    public NormalPosts addNewPost(@NonNull final NormalPosts newNormalPost) {
        // 존재하는 유저인지 확인
        usersRepository.findById(newNormalPost.getUserId())
                .orElseThrow(() -> new UserNotFoundException("Error: 존재하지 않는 유저"));
        return normalPostsRepository.save(newNormalPost);
    }

    /**
     * 일반 게시물 수정
     *
     * @param updatingNormalPost 수정할 일반 게시물
     * @return {@code NormalPosts}
     */
    @Transactional
    public NormalPosts updateNormalPost(@NonNull final NormalPosts updatingNormalPost) {
        Users targetUser = usersRepository.findById(updatingNormalPost.getUserId())
                .orElseThrow(() -> new UserNotFoundException());

        // 권한이 없는 유저는 게시물 획득 불가
        NormalPosts targetPost = normalPostsRepository.findByNormalPostIdAndUserId(
                updatingNormalPost.getNormalPostId(), targetUser.getUserId())
                .orElseThrow(() -> new PostNotFoundException());

        NormalPosts updatedPost = targetPost.update(updatingNormalPost);
        return updatedPost;
    }
}
