package com.leadpet.www.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * UnexpectedOperationException
 */
public class UnexpectedOperationException extends ResponseStatusException {
    public UnexpectedOperationException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "Error: 예상외 에러");
    }

    public UnexpectedOperationException(final String reason) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, reason);
    }

    public UnexpectedOperationException(final String reason, final Throwable cause) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, reason, cause);
    }
}
