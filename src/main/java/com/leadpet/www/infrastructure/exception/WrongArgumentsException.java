package com.leadpet.www.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * WrongArgumentsException
 */
public class WrongArgumentsException extends ResponseStatusException {
    public WrongArgumentsException(final String reason) {
        super(HttpStatus.BAD_REQUEST, reason);
    }
    public WrongArgumentsException(final String reason, final Throwable throwable) {
        super(HttpStatus.BAD_REQUEST, reason, throwable);
    }
}
