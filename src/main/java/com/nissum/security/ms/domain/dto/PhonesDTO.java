package com.nissum.security.ms.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PhonesDTO {

	private String number;
	private String contryCode;
	private String cityCode;
}