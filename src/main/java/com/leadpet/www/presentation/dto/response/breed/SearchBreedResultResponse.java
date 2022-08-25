package com.leadpet.www.presentation.dto.response.breed;

import io.swagger.annotations.ApiModel;
import lombok.AccessLevel;

import java.util.List;

/**
 * SearchBreedResultResponse
 */
@ApiModel("품종 리스트 결과 Response")
@lombok.Getter
@lombok.Builder
@lombok.NoArgsConstructor(access = AccessLevel.PROTECTED)
@lombok.AllArgsConstructor
public class SearchBreedResultResponse {
    private List<Result> results;

    /**
     * 품종 리스트 검색 결과
     */
    @lombok.Getter
    @lombok.Builder
    @lombok.NoArgsConstructor(access = AccessLevel.PROTECTED)
    @lombok.AllArgsConstructor
    public static class Result {
        private String index;
        private List<SearchBreedResponse> breedList;
    }
}
