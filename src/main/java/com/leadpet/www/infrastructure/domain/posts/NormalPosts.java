package com.leadpet.www.infrastructure.domain.posts;

import com.leadpet.www.infrastructure.db.converter.StringListConverter;
import com.leadpet.www.infrastructure.domain.BaseTime;
import com.leadpet.www.infrastructure.domain.users.Users;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

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
    @Column(name = "normal_post_id", unique = true, nullable = false)
    private String normalPostId;

    @Column(nullable = false)
    private String title;
    @Lob
    @Column(nullable = false)
    private String contents;
    @Convert(converter = StringListConverter.class)
    private List<String> images;
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
        if (Objects.nonNull(updatingNormalPost.getTitle())) {
            this.title = updatingNormalPost.getTitle();
        }
        if (Objects.nonNull(updatingNormalPost.getContents())) {
            this.contents = updatingNormalPost.getContents();
        }
        // TODO image 수정은 전체 리스트를 받게 되는게 맞나? 그게 아니라면 로직 변경 필요.
        if (Objects.nonNull(updatingNormalPost.getImages())) {
            this.images = updatingNormalPost.getImages();
        }
        return this;
    }

}
