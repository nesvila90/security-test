package com.nissum.security.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record UserRequestDTO(
        String password,
        String name,
        @NotNull
        List<PhonesDTO> phones,
        @NotNull
        @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Email invalid format")
        String email
) {
}
