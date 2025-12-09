package com.rohan.dev.course.exceptions;

import org.springframework.security.core.AuthenticationException;

public class MfaRequiredException extends AuthenticationException {
    public MfaRequiredException(String message) {
        super(message);
    }
}
