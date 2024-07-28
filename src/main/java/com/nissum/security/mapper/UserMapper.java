package com.nissum.security.mapper;


import com.nissum.security.domain.dto.UserRequestDTO;
import com.nissum.security.domain.dto.UserResponseDTO;
import com.nissum.security.domain.entities.RoleEntity;
import com.nissum.security.domain.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

import java.time.LocalDateTime;
import java.util.Set;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = PhonesMapper.class
)

public interface UserMapper {

    @Mapping(target = "active", defaultValue = "true", ignore = true)
    @Mapping(target = "modified", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(source = "user.name", target = "name")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.phones", target = "phones")
    @Mapping(source = "passwordEncoded", target = "password")
    @Mapping(source = "token", target = "token")
    @Mapping(source = "lastLogin", target = "lastLogin")
    @Mapping(source = "roles", target = "roles")
    UserEntity toEntity(UserRequestDTO user,
                        String username,
                        String passwordEncoded,
                        String token,
                        LocalDateTime lastLogin,
                        Set<RoleEntity> roles);


    @Mapping(target = "isActive", source = "active")
    UserResponseDTO toDto(UserEntity user);
}
