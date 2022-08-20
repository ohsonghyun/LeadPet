package com.leadpet.www.infrastructure.domain.posts;

import com.leadpet.www.infrastructure.db.converter.StringListConverter;
import com.leadpet.www.infrastructure.domain.BaseTime;
import com.leadpet.www.infrastructure.domain.pet.AnimalType;
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
public class AdoptionPosts extends BaseTime {

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

    @Lob
    @Column(name = "contents")
    private String contents;

    @Column(name = "animal_type")
    @Enumerated(value = EnumType.STRING)
    private AnimalType animalType;

    @Column(name = "breed")
    private String breed;

    @Column(name = "gender")
    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @Column(name = "neutering")
    @Enumerated(value = EnumType.STRING)
    private Neutering neutering;

    @Column(name = "age")
    private Integer age;

    @Column(name = "disease")
    private String disease;

    @Convert(converter = StringListConverter.class)
    private List<String> images;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

}
