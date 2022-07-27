package com.leadpet.www.application.service.reply.normal;

import com.leadpet.www.infrastructure.db.normalPost.NormalPostsRepository;
import com.leadpet.www.infrastructure.db.reply.NormalReplyRepository;
import com.leadpet.www.infrastructure.db.users.UsersRepository;
import com.leadpet.www.infrastructure.domain.posts.NormalPosts;
import com.leadpet.www.infrastructure.domain.reply.normal.NormalReply;
import com.leadpet.www.infrastructure.domain.users.Users;
import com.leadpet.www.infrastructure.exception.PostNotFoundException;
import com.leadpet.www.infrastructure.exception.ReplyNotFoundException;
import com.leadpet.www.infrastructure.exception.UnauthorizedUserException;
import com.leadpet.www.infrastructure.exception.login.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * NormalReplyService
 */
@Service
@Slf4j
@lombok.RequiredArgsConstructor
public class NormalReplyService {

    private final UsersRepository usersRepository;
    private final NormalPostsRepository normalPostsRepository;
    private final NormalReplyRepository normalReplyRepository;

    /**
     * 새로운 댓글 추가(일상피드)
     *
     * @param normalPostId {@code String} 일상피드ID
     * @param userId       {@code String} 작성유저ID
     * @param content      {@code String} 댓글 내용
     * @return {@code NormalReply} 추가한 댓글
     */
    public NormalReply saveReply(final String normalPostId, final String userId, final String content) {
        NormalPosts normalPosts = normalPostsRepository.findById(normalPostId)
                .orElseThrow(() -> {
                    log.error("[NormalReplyService] 존재하지 않는 일상 피드 ID: {}", normalPostId);
                    return new PostNotFoundException("Error: 존재하지 않는 일상 피드");
                });

        Users user = usersRepository.findByUserId(userId);
        if (Objects.isNull(user)) {
            log.error("[NormalReplyService] 존재하지 않는 유저 ID: {}", userId);
            throw new UserNotFoundException("Error: 존재하지 않는 유저");
        }

        return normalReplyRepository.save(
                NormalReply.builder()
                        .normalReplyId(String.format("NR_%s", RandomStringUtils.random(10, true, true)))
                        .normalPost(normalPosts)
                        .userId(userId)
                        .content(content)
                        .build());
    }

    /**
     * 댓글 삭제
     *
     * @param userId  {@code String} 삭제 요청 유저ID
     * @param replyId {@code String} 삭제 대상 댓글ID
     * @return {@code String} 삭제한 댓글ID
     */
    public String deleteReply(final String userId, final String replyId) {
        NormalReply deletingReply = normalReplyRepository.findById(replyId)
                .orElseThrow(() -> {
                    log.error("[NormalReplyService] 존재하지 않는 댓글 ID: {}", replyId);
                    return new ReplyNotFoundException("Error: 존재하지 않는 댓글");
                });

        if (!ObjectUtils.defaultIfNull(deletingReply.getUserId(), StringUtils.EMPTY).equals(userId)) {
            log.error("[NormalReplyService] 삭제 권한이 없는 유저: {}", userId);
            throw new UnauthorizedUserException("Error: 삭제 권한이 없는 유저");
        }

        return replyId;
    }
}
