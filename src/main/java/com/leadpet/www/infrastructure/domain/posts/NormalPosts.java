package com.leadpet.www.infrastructure.domain.posts;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * NormalPosts
 */
@Entity
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.Builder
public class NormalPosts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long normalPostId;

    private String title;
    private String contents;

    private Long userId;

    @Override
    public String toString() {
        return String.format("postId: %s, title: %s, contents: %s, userId: %s", this.normalPostId, this.title, this.contents, this.userId);
    }
}
