package com.nissum.security.ms.domain.enums;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum NissumExceptionsTypes {

    BAD_REQUEST("NSBR-001", "Petición Invalida.", HttpStatus.BAD_REQUEST),
    TECHNICAL_ERROR("NSBR-002", "Error Interno en la API.", HttpStatus.INTERNAL_SERVER_ERROR),
    BUSINESS_ERROR("NSBR-003", "Business Error.", HttpStatus.CONFLICT),
    FORBIDDEN("NSBR-004", "Forbidden.", HttpStatus.FORBIDDEN),
    UNAUTHORIZED("NSBR-005", "Unauthorized.", HttpStatus.UNAUTHORIZED),;


    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
