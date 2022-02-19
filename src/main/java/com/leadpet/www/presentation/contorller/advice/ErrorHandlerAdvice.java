package com.leadpet.www.presentation.contorller.advice;

import com.leadpet.www.infrastructure.error.signup.UserAlreadyExistsException;
import com.leadpet.www.presentation.dto.response.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ErrorHandlerAdvice {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handler(UserAlreadyExistsException ex) {
        return errorResponse(ex);
    }

    /**
     * 에러 리스폰스 생성
     * @param ex 발생 에러
     */
    private ResponseEntity<ErrorResponse> errorResponse(ResponseStatusException ex) {
        final ErrorResponse errorResponse = new ErrorResponse(ex.getStatus(), ex.getReason());
        final HttpHeaders headers =  new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(errorResponse, headers, ex.getStatus());
    }
}
