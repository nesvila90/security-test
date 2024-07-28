package com.nissum.security.ms.controller;

import com.nissum.security.ms.domain.dto.error.BaseErrorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RestControllerAdviser {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<BaseErrorDTO> handleGeneralException(final Exception ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new BaseErrorDTO(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
