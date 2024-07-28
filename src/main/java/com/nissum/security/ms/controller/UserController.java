package com.nissum.security.ms.controller;


import com.nissum.security.ms.domain.dto.UserRequestDTO;
import com.nissum.security.ms.domain.dto.UserResponseDTO;
import com.nissum.security.ms.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserServiceImpl userService;

    public ResponseEntity<UserResponseDTO> createUser(UserRequestDTO userRequestDTO) {
        return ResponseEntity.ok(userService.createUser(userRequestDTO));
    }


}
