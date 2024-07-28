package com.nissum.security.ms.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UserRequestDTO(
        String password,
        String name,
        @NotNull
        List<PhonesDTO> phones,
        @NotNull
        @Email(regexp = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", message = "Email invalid format")
        String email
) {
}
