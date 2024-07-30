package com.nissum.security.controller;

import com.nissum.security.domain.dto.error.BaseErrorDTO;
import com.nissum.security.domain.exceptions.NissumException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class RestControllerAdviser {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<BaseErrorDTO> handleGeneralException(final Exception ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new BaseErrorDTO(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = NissumException.class)
    public ResponseEntity<BaseErrorDTO> handleNissumException(final NissumException ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new BaseErrorDTO(ex.getExceptionType().getMessage()), ex.getExceptionType().getHttpStatus());
    }

    @ExceptionHandler(value = {ConstraintViolationException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<BaseErrorDTO> handleBadRequestException(final Exception ex) {
        log.error(ex.getMessage(), ex);
        if (ex instanceof MethodArgumentNotValidException cex) {
            return new ResponseEntity<>(new BaseErrorDTO(Objects.requireNonNull(cex.getBindingResult().getFieldError()).getDefaultMessage()), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new BaseErrorDTO(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
