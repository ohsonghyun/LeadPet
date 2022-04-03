package com.leadpet.www.presentation.dto.response.post;

import io.swagger.annotations.ApiModel;

/**
 * NormalPostAllCountResponse
 */
@ApiModel("일반 게시물 총 카운트 Response")
@lombok.Getter
@lombok.Builder
public class NormalPostAllCountResponse {

    private Long allCount;

}
