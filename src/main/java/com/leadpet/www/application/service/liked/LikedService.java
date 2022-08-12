package com.leadpet.www.application.service.liked;

import com.leadpet.www.infrastructure.db.liked.LikedRepository;
import com.leadpet.www.infrastructure.domain.liked.Liked;
import com.leadpet.www.infrastructure.domain.liked.LikedResult;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * LikedService
 */
@Service
@Transactional
@lombok.RequiredArgsConstructor
public class LikedService {

    private final LikedRepository likedRepository;

    /**
     * 좋아요 데이터 갱신
     * <ul>
     *     <li>좋아요 데이터가 존재하지 않는 경우에는 좋아요 저장</li>
     *     <li>좋아요 데이터가 존재하는 경우에는 좋아요 삭제</li>
     * </ul>
     *
     * @param userId {@code String}
     * @param postId {@code String}
     */
    public LikedResult saveOrDelete(final String userId, final String postId) {
        Liked likedInDB = likedRepository.findByUserIdAndPostId(userId, postId);
        if (Objects.isNull(likedInDB)) {
            likedRepository.save(
                    Liked.builder()
                            .likedId(String.format("%s_%s", userId, RandomStringUtils.random(10, true, true)))
                            .userId(userId)
                            .postId(postId)
                            .build());
            return LikedResult.CREATED;
        }
        likedRepository.deleteByUserIdAndPostId(userId, postId);
        return LikedResult.DELETED;
    }
}
