package com.leadpet.www.application.service;

import com.leadpet.www.infrastructure.db.NormalPostsRepository;
import com.leadpet.www.infrastructure.db.UsersRepository;
import com.leadpet.www.infrastructure.domain.posts.NormalPosts;
import com.leadpet.www.infrastructure.domain.users.LoginMethod;
import com.leadpet.www.infrastructure.domain.users.Users;
import com.leadpet.www.infrastructure.exception.login.UserNotFoundException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

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
    public NormalPosts addNewPost(@NonNull final NormalPosts newNormalPost, @NonNull final String uid,
                                  @NonNull final LoginMethod loginMethod) {
        Users user = usersRepository.findByLoginMethodAndUid(loginMethod, uid);
        if (Objects.isNull(user)) {
            throw new UserNotFoundException("Error: 존재하지 않는 유저");
        }
        // 불변성 유지를 위해 객체 새로 생성. 생성비용이 크지 않기 때문에 괜찮지 않을까. 문제가 생기면 그 때 생각해보는 걸로.
        NormalPosts normalPostWithUserId = NormalPosts.builder()
                .title(newNormalPost.getTitle())
                .contents(newNormalPost.getContents())
                .images(newNormalPost.getImages())
                .tags(newNormalPost.getTags())
                .userId(user.getUserId())
                .build();
        return normalPostsRepository.save(normalPostWithUserId);
    }
}
