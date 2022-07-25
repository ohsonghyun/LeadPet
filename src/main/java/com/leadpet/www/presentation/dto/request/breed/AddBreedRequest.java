package com.leadpet.www.presentation.dto.request.breed;

import io.swagger.annotations.ApiModel;

/**
 * AddBreedRequest
 */
@ApiModel("품종 추가 리퀘스트")
@lombok.Getter
@lombok.Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class AddBreedRequest {
    private String category;
    private String breedName;
}
