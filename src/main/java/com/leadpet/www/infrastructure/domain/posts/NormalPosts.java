package com.leadpet.www.infrastructure.domain.posts;

import com.leadpet.www.infrastructure.db.converter.StringListConverter;
import com.leadpet.www.infrastructure.domain.BaseTime;
import com.leadpet.www.infrastructure.domain.users.Users;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.List;

/**
 * NormalPosts
 */
@Entity
@lombok.Getter
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.Builder
public class NormalPosts extends BaseTime {

    @Id
    @Column(unique = true, nullable = false)
    private String normalPostId;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String contents;
    @Convert(converter = StringListConverter.class)
    private List<String> images;
    @Convert(converter = StringListConverter.class)
    private List<String> tags;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    /**
     * 일반게시글 수정
     *
     * @param updatingNormalPost 수정할 내용을 담은 일반 게시글 오브젝트
     * @return {@code NormalPosts} 수정 후 일반 게시글 오브젝트
     */
    @NonNull
    public NormalPosts update(@NonNull final NormalPosts updatingNormalPost) {
        this.title = updatingNormalPost.getTitle();
        this.contents = updatingNormalPost.getContents();
        this.images = updatingNormalPost.getImages();
        this.tags = updatingNormalPost.getTags();
        return this;
    }

}
