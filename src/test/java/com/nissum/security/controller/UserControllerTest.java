package com.nissum.security.controller;

import com.nissum.security.domain.dto.PhonesDTO;
import com.nissum.security.domain.dto.UserRequestDTO;
import com.nissum.security.domain.dto.UserResponseDTO;
import com.nissum.security.security.jwt.JwtTokenProvider;
import com.nissum.security.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@ComponentScan(basePackages = "com.nissum.security") // Añade el paquete base para escanear los componentes
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider; // Añade el mock para JwtTokenProvider

    private UserRequestDTO userRequestDTO;
    private UserResponseDTO userResponseDTO;

    @BeforeEach
    void setUp() {
        PhonesDTO phone = new PhonesDTO("1234567890", "1", "57");
        userRequestDTO = UserRequestDTO.builder()
                .name("Test User")
                .email("testuser@example.com")
                .password("password123")
                .phones(List.of(phone))
                .build();

        userResponseDTO = new UserResponseDTO(
                UUID.randomUUID(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                "some-token",
                true
        );
    }

    @Test
    void testCreateUser() throws Exception {
        Mockito.when(userService.createUser(any(UserRequestDTO.class))).thenReturn(userResponseDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test User\",\"email\":\"testuser@example.com\",\"password\":\"password123\",\"phones\":[{\"number\":\"1234567890\",\"cityCode\":\"1\",\"countryCode\":\"57\"}]}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.token", is("some-token")))
                .andExpect(jsonPath("$.isActive", is(true)));
    }
}