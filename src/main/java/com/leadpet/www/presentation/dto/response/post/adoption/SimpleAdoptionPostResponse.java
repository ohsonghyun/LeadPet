package com.leadpet.www.presentation.dto.response.post.adoption;

import com.leadpet.www.infrastructure.domain.pet.AnimalType;
import com.leadpet.www.infrastructure.domain.pet.Gender;
import com.leadpet.www.infrastructure.domain.pet.Neutering;
import io.swagger.annotations.ApiModel;

import java.time.LocalDateTime;
import java.util.List;

/**
 * SimpleAdoptionPostResponse
 */
@ApiModel("Simple 입양 게시물 Response")
@lombok.Getter
@lombok.RequiredArgsConstructor
@lombok.Builder
public class SimpleAdoptionPostResponse {
    private final String adoptionPostId;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final LocalDateTime euthanasiaDate;
    private final String title;
    private final String contents;
    private final AnimalType animalType;
    private final String breed;
    private final Gender gender;
    private final Neutering neutering;
    private final Integer age;
    private final String disease;
    private final List<String> images;
    private final String userId;
    private final String userName;
    private final String profileImage;
}
