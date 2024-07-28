package com.nissum.security.ms.domain.entities;

import jakarta.persistence.Embeddable;

@Embeddable
public class Phones {

	private String number;
	private String countryCode;
	private String cityCode;
}