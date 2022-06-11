package com.leadpet.www.infrastructure.domain.posts;

import com.leadpet.www.infrastructure.db.converter.StringListConverter;
import com.leadpet.www.infrastructure.domain.BaseTime;
import com.leadpet.www.infrastructure.domain.users.Users;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DonationPosts
 */
@Entity
@lombok.Getter
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.Builder
public class DonationPosts extends BaseTime {

    @Id
    private String donationPostId;

    /**
     * 게시 시작일
     */
    @Column(name = "start_date")
    private LocalDateTime startDate;

    /**
     * 게시 종료일
     */
    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "title")
    private String title;

    // TODO to enum; 후원 계좌, 물품 배송
    @Column(name = "donation_method")
    private String donationMethod;

    @Lob
    @Column(name = "contents")
    private String contents;

    @Column(name = "images")
    @Convert(converter = StringListConverter.class)
    private List<String> images;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

}
