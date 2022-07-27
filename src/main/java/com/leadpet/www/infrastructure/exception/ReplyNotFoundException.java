package com.leadpet.www.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * ReplyNotFoundException
 */
public class ReplyNotFoundException extends ResponseStatusException {
    public ReplyNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Error: 존재하지 않는 댓글");
    }

    public ReplyNotFoundException(final String reason) {
        super(HttpStatus.NOT_FOUND, reason);
    }

    public ReplyNotFoundException(final String reason, final Throwable cause) {
        super(HttpStatus.NOT_FOUND, reason, cause);
    }
}
