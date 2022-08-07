package com.leadpet.www.presentation.dto.request.post.adoption;

import com.leadpet.www.infrastructure.domain.pet.AnimalType;
import com.leadpet.www.infrastructure.domain.pet.Gender;
import com.leadpet.www.infrastructure.domain.pet.Neutering;
import com.leadpet.www.infrastructure.domain.posts.AdoptionPosts;
import io.swagger.annotations.ApiModel;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * AddAdoptionPostRequestDto
 */
@ApiModel("신규 입양 게시물 Request")
@lombok.Getter
@lombok.Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class AddAdoptionPostRequestDto {

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime euthanasiaDate;
    private String title;
    private String contents;
    private AnimalType animalType;
    private String breed;
    private Gender gender;
    private Neutering neutering;
    private Integer age;
    private String disease;
    private List<String> images;
    @NotNull
    private String userId;

    /**
     * 입양 게시물 객체로 변환
     * <p>AdoptionPosts의 userId는 백엔드에서 취득해서 저장</p>
     *
     * @return {@code Adoption}
     */
    public AdoptionPosts toAdoptionPost() {
        return AdoptionPosts.builder()
                .startDate(startDate)
                .endDate(endDate)
                .euthanasiaDate(euthanasiaDate)
                .title(title)
                .contents(contents)
                .animalType(animalType)
                .breed(breed)
                .gender(gender)
                .neutering(neutering)
                .age(age)
                .disease(disease)
                .images(images)
                .build();
    }
}
