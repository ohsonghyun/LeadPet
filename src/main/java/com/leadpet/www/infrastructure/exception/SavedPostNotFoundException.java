package com.leadpet.www.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * SavedPostNotFoundException
 */
public class SavedPostNotFoundException extends ResponseStatusException {
    public SavedPostNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Error: 존재하지 않는 저장피드");
    }

    public SavedPostNotFoundException(final String reason) {
        super(HttpStatus.NOT_FOUND, reason);
    }

    public SavedPostNotFoundException(final String reason, final Throwable cause) {
        super(HttpStatus.NOT_FOUND, reason, cause);
    }
}
