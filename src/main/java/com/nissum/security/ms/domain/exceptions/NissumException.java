package com.nissum.security.ms.domain.exceptions;

import com.nissum.security.ms.domain.enums.NissumExceptionsTypes;

public class NissumException extends RuntimeException {

    private final NissumExceptionsTypes exceptionType;

    public NissumException(String message, NissumExceptionsTypes exceptionType) {
        super(message);
        this.exceptionType = exceptionType;
    }

    public NissumException(String message, Throwable cause, NissumExceptionsTypes exceptionType) {
        super(message, cause);
        this.exceptionType = exceptionType;
    }
}
