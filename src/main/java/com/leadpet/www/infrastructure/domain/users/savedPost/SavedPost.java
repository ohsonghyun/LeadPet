package com.leadpet.www.infrastructure.domain.users.savedPost;

import com.leadpet.www.infrastructure.domain.BaseTime;
import com.leadpet.www.infrastructure.domain.posts.PostType;
import com.leadpet.www.infrastructure.domain.users.Users;
import lombok.AccessLevel;

import javax.persistence.*;

/**
 * SavedPost
 * <p>유저가 저장한 피드</p>
 */
@Entity
@lombok.Getter
@lombok.Builder
@lombok.NoArgsConstructor(access = AccessLevel.PROTECTED)
@lombok.AllArgsConstructor
public class SavedPost extends BaseTime {

    @Id
    @Column(name = "saved_post_id")
    private String savedPostId;

    /**
     * 일상/기부/입양 피드 각각의 id
     */
    @Column(name = "post_id")
    private String postId;

    @Enumerated(EnumType.STRING)
    @Column(name = "post_type")
    private PostType postType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

}
