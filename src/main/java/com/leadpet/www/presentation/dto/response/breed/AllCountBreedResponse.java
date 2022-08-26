package com.leadpet.www.presentation.dto.response.breed;

import io.swagger.annotations.ApiModel;

/**
 * AllCountBreedResponse
 */
@ApiModel("품종 카운트 Response")
@lombok.Getter
@lombok.Builder
public class AllCountBreedResponse {
    private long count;
}
