package com.leadpet.www.infrastructure.error.signup;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * UserAlreadyExistsException
 */
public class UserAlreadyExistsException extends ResponseStatusException {
    public UserAlreadyExistsException(final String reason) {
        super(HttpStatus.CONFLICT, reason);
    }
    public UserAlreadyExistsException(final String reason, final Throwable cause) {
        super(HttpStatus.CONFLICT, reason, cause);
    }
}
