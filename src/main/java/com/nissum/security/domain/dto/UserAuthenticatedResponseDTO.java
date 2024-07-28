package com.nissum.security.domain.dto;

import java.util.List;

public record UserAuthenticatedResponseDTO(String jwt, Object id, String username, Object email, List<String> roles) {
}
