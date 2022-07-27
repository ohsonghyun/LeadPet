package com.leadpet.www.infrastructure.domain.reply.normal;

import com.leadpet.www.infrastructure.domain.posts.NormalPosts;
import lombok.AccessLevel;

import javax.persistence.*;

/**
 * NormalReply
 */
@Entity
@lombok.Getter
@lombok.Builder
@lombok.NoArgsConstructor(access = AccessLevel.PROTECTED)
@lombok.AllArgsConstructor
public class NormalReply {

    @Id
    @Column(name = "normal_reply_id")
    private String normalReplyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "normal_post_id")
    private NormalPosts normalPost;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "content")
    private String content;

}
