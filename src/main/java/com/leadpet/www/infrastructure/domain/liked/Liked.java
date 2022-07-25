package com.leadpet.www.infrastructure.domain.liked;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Liked
 * <p>좋아요 도메인</p>
 */
@Entity
@lombok.Getter
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.Builder
public class Liked {
    @Id
    @Column(name = "liked_id")
    private String likedId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "post_id")
    private String postId;
}
