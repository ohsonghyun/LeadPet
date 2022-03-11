package com.leadpet.www.infrastructure.exception.login;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * UserNotFoundException
 */
public class UserNotFoundException extends ResponseStatusException {
    public UserNotFoundException(final String reason) {
        super(HttpStatus.NOT_FOUND, reason);
    }
    public UserNotFoundException(final String reason, final Throwable cause) {
        super(HttpStatus.NOT_FOUND, reason, cause);
    }
}
