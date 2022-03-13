package com.leadpet.www.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * PostNotFoundException
 */
public class PostNotFoundException extends ResponseStatusException {
    public PostNotFoundException(final String reason) {
        super(HttpStatus.NOT_FOUND, reason);
    }
    public PostNotFoundException(final String reason, final Throwable cause) {
        super(HttpStatus.NOT_FOUND, reason, cause);
    }
}
