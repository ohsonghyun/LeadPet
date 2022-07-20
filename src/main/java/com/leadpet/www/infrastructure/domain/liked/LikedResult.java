package com.leadpet.www.infrastructure.domain.liked;

/**
 * LikedResult
 * <p>좋아요 처리 구분</p>
 */
@lombok.AllArgsConstructor
public enum LikedResult {
    CREATED("201"),
    DELETED("200"),
    ;

    private String code;
}
