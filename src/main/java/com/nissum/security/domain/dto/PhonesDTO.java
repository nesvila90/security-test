package com.nissum.security.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PhonesDTO {

    private String number;
    private String countryCode;
    private String cityCode;
}