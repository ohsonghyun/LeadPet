package com.leadpet.www.presentation.dto.response.breed;

import com.leadpet.www.infrastructure.domain.breed.Breed;
import io.swagger.annotations.ApiModel;

/**
 * AddBreedResponse
 */
@ApiModel("품종 추가 Response")
@lombok.Getter
@lombok.Builder(access = lombok.AccessLevel.PRIVATE)
public class AddBreedResponse {
    private String breedId;
    private String category;
    private String breedName;

    public static AddBreedResponse from(final Breed breed) {
        return AddBreedResponse.builder()
                .breedId(breed.getBreedId())
                .category(breed.getCategory())
                .breedName(breed.getBreedName())
                .build();
    }
}
