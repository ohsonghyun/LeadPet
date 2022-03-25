package com.leadpet.www.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * UnauthorizedUserException
 */
public class UnauthorizedUserException extends ResponseStatusException {
    public UnauthorizedUserException() {
        super(HttpStatus.FORBIDDEN, "Error: 권한 없는 조작");
    }
    public UnauthorizedUserException(final String reason) {
        super(HttpStatus.FORBIDDEN, reason);
    }
    public UnauthorizedUserException(final String reason, final Throwable cause) {
        super(HttpStatus.FORBIDDEN, reason, cause);
    }
}
