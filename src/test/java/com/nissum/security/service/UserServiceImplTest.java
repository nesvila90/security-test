package com.nissum.security.service;


import com.nissum.security.domain.dto.PhonesDTO;
import com.nissum.security.domain.dto.UserRequestDTO;
import com.nissum.security.domain.dto.UserResponseDTO;
import com.nissum.security.domain.entities.UserEntity;
import com.nissum.security.domain.exceptions.NissumException;
import com.nissum.security.mapper.UserMapper;
import com.nissum.security.persistence.UserRepository;
import com.nissum.security.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.nissum.security.domain.enums.NissumExceptionsTypes.USER_ALREADY_EXIST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRequestDTO userRequestDTO;
    private UserEntity userEntity;
    private UserResponseDTO userResponseDTO;
    private final String encodedPassword = "encodedPassword";
    private final String token = "jwtToken";
    private final String username = "testuser";

    @BeforeEach
    void setUp() {
        userRequestDTO = UserRequestDTO.builder()
                .name("Test User")
                .email("testuser@example.com")
                .password("password123")
                .phones(List.of(new PhonesDTO("1234567890", "1", "57")))
                .build();

        userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(encodedPassword);
        userEntity.setToken(token);

        userResponseDTO = new UserResponseDTO(
                UUID.randomUUID(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                token,
                true
        );
    }

    @Test
    void loadUserByUsername_UserFound() {
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));

        User userDetails = (User) userService.loadUserByUsername(username);

        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals(encodedPassword, userDetails.getPassword());
    }

    @Test
    void loadUserByUsername_UserNotFound() {
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(username));
    }

    @Test
    void createUser_Success() {
        when(passwordEncoder.encode(userRequestDTO.password())).thenReturn(encodedPassword);
        when(tokenProvider.generateToken(any(Authentication.class))).thenReturn(token);
        when(userMapper.toEntity(any(), anyString(), anyString(), anyString(), any(), any())).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(userMapper.toDto(userEntity)).thenReturn(userResponseDTO);

        UserResponseDTO response = userService.createUser(userRequestDTO);

        assertNotNull(response);
        assertEquals(token, response.token());
    }

    @Test
    void createUser_UserAlreadyExists() {
        when(passwordEncoder.encode(userRequestDTO.password())).thenReturn(encodedPassword);
        when(tokenProvider.generateToken(any(Authentication.class))).thenReturn(token);
        when(userMapper.toEntity(any(), anyString(), anyString(), anyString(), any(), any())).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenThrow(new DataIntegrityViolationException("User already exists"));

        NissumException exception = assertThrows(NissumException.class, () -> userService.createUser(userRequestDTO));

        assertEquals(USER_ALREADY_EXIST, exception.getExceptionType());
    }
}