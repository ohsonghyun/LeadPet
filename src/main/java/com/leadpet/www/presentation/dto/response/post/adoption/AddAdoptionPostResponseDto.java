package com.leadpet.www.presentation.dto.response.post.adoption;

import com.leadpet.www.infrastructure.domain.pet.AnimalType;
import com.leadpet.www.infrastructure.domain.pet.Gender;
import com.leadpet.www.infrastructure.domain.pet.Neutering;
import com.leadpet.www.infrastructure.domain.posts.AdoptionPosts;
import com.leadpet.www.infrastructure.domain.posts.DonationPosts;
import io.swagger.annotations.ApiModel;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * AddAdoptionPostResponseDto
 */
@ApiModel("신규 입양 게시물 Response")
@lombok.Getter
@lombok.Builder(access = lombok.AccessLevel.PRIVATE)
public class AddAdoptionPostResponseDto {

    private String adoptionPostId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime euthanasiaDate;
    private String title;
    private String contents;
    private AnimalType animalType;
    private String species;
    private Gender gender;
    private Neutering neutering;
    private Integer age;
    private String disease;
    private List<String> images;
    private String userId;

    public static AddAdoptionPostResponseDto from(@NonNull final AdoptionPosts adoptionPost) {
        return AddAdoptionPostResponseDto.builder()
                .adoptionPostId(adoptionPost.getAdoptionPostId())
                .startDate(adoptionPost.getStartDate())
                .endDate(adoptionPost.getEndDate())
                .euthanasiaDate(adoptionPost.getEuthanasiaDate())
                .title(adoptionPost.getTitle())
                .contents(adoptionPost.getContents())
                .animalType(adoptionPost.getAnimalType())
                .species(adoptionPost.getSpecies())
                .gender(adoptionPost.getGender())
                .neutering(adoptionPost.getNeutering())
                .age(adoptionPost.getAge())
                .disease(adoptionPost.getDisease())
                .images(adoptionPost.getImages())
                .userId(adoptionPost.getUser().getUserId())
                .build();
    }
}
