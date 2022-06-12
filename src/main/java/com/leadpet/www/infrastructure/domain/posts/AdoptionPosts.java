package com.leadpet.www.infrastructure.domain.posts;

import com.leadpet.www.infrastructure.db.converter.StringListConverter;
import com.leadpet.www.infrastructure.domain.pet.Gender;
import com.leadpet.www.infrastructure.domain.pet.Neutering;
import com.leadpet.www.infrastructure.domain.users.Users;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * AdoptionPosts
 */
@Entity
@lombok.Getter
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.Builder
public class AdoptionPosts {

    @Id
    @Column(name = "adoption_post_id")
    private String adoptionPostId;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "euthanasia_date")
    private LocalDateTime euthanasiaDate;

    @Column(name = "title")
    private String title;

    @Column(name = "contents")
    private String contents;

    // TODO enum
    @Column(name = "animal_type")
    private String animalType;

    @Column(name = "species")
    private String species;

    @Column(name = "gender")
    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @Column(name = "neutering")
    @Enumerated(value = EnumType.STRING)
    private Neutering neutering;

    @Convert(converter = StringListConverter.class)
    private List<String> images;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

}
