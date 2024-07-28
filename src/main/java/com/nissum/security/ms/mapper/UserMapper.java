package com.nissum.security.ms.mapper;


import com.nissum.security.ms.domain.dto.UserRequestDTO;
import com.nissum.security.ms.domain.dto.UserResponseDTO;
import com.nissum.security.ms.domain.entities.RoleEntity;
import com.nissum.security.ms.domain.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.Set;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(
        componentModel = "spring"
)
public interface UserMapper {

    @Mapping(target = "active", defaultValue = "true")
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
