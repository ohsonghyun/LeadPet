package com.leadpet.www.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * UnsatisfiedRequirementException
 */
public class UnsatisfiedRequirementException extends ResponseStatusException {
    public UnsatisfiedRequirementException(String reason) {
        super(HttpStatus.BAD_REQUEST, reason);
    }
    public UnsatisfiedRequirementException(final String reason, final Throwable cause) {
        super(HttpStatus.BAD_REQUEST, reason, cause);
    }
}
