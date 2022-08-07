package com.leadpet.www.presentation.dto.response.breed;

import com.leadpet.www.infrastructure.domain.breed.Breed;
import com.leadpet.www.infrastructure.domain.pet.AnimalType;
import io.swagger.annotations.ApiModel;
import lombok.AccessLevel;

/**
 * BreedListResponse
 */
@ApiModel("품종 리스트 단일 Response")
@lombok.Getter
@lombok.Builder(access = AccessLevel.PRIVATE)
@lombok.NoArgsConstructor(access = AccessLevel.PROTECTED)
@lombok.AllArgsConstructor
public class SearchBreedResponse {
    private String breedName;
    private AnimalType animalType;

    public static SearchBreedResponse from(final Breed breed) {
        return SearchBreedResponse.builder()
                .breedName(breed.getBreedName())
                .animalType(breed.getAnimalType())
                .build();
    }
}
