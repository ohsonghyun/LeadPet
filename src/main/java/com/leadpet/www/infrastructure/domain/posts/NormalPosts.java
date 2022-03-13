package com.leadpet.www.infrastructure.domain.posts;

import com.leadpet.www.infrastructure.db.converter.StringListConverter;
import com.leadpet.www.infrastructure.domain.BaseTime;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long normalPostId;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String contents;
    @Convert(converter = StringListConverter.class)
    private List<String> images;
    @Convert(converter = StringListConverter.class)
    private List<String> tags;

    @Column(nullable = false)
    private Long userId;

}
