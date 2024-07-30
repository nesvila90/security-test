package com.nissum.security.domain.dto;

import com.nissum.security.domain.annotations.NissumEmailValidator;
import com.nissum.security.domain.annotations.NissumPasswordValidator;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record UserRequestDTO(

        @NissumPasswordValidator(message = "Password not met security conditions.")
        String password,
        String name,
        @NotNull
        List<PhonesDTO> phones,
        @NotNull
        @NissumEmailValidator(message = "Email invalid format")
        String email
) {
}
