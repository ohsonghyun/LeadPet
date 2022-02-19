package com.leadpet.www.presentation.dto.response;

import org.springframework.http.HttpStatus;

@lombok.Getter
public class ErrorResponse {
    private Error error;

    public ErrorResponse(HttpStatus httpStatus, String detail) {
        this.error = new Error(httpStatus.value(), httpStatus, detail);
    }

    /**
     * 에러 정보
     */
    @lombok.Getter
    @lombok.AllArgsConstructor
    public static class Error {
        /**
         * HTTP status code
         */
        private Integer code;

        /**
         * ERROR message
         */
        private HttpStatus message;

        /**
         * ERROR 세부내용
         */
        private String detail;
    }
}
