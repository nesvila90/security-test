package com.nissum.security.controller;


import com.nissum.security.domain.dto.UserRequestDTO;
import com.nissum.security.domain.enums.NissumExceptionsTypes;
import com.nissum.security.domain.exceptions.NissumException;
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

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {UserController.class, RestControllerAdviser.class})
@ComponentScan(basePackages = "com.nissum.security")
class RestControllerAdviserTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        // Setup code if needed
    }

    @Test
    void testHandleGeneralException() throws Exception {
        doThrow(new RuntimeException("General error")).when(userService).createUser(Mockito.any(UserRequestDTO.class));

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test User\",\"email\":\"testuser@example.com\",\"password\":\"password123\",\"phones\":[{\"number\":\"1234567890\",\"cityCode\":\"1\",\"countryCode\":\"57\"}]}"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorMessage", is("General error")));
    }

    @Test
    void testHandleNissumException() throws Exception {
        NissumException nissumException = new NissumException("Nissum error", NissumExceptionsTypes.USER_ALREADY_EXIST);
        doThrow(nissumException).when(userService).createUser(Mockito.any(UserRequestDTO.class));

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test User\",\"email\":\"testuser@example.com\",\"password\":\"password123\",\"phones\":[{\"number\":\"1234567890\",\"cityCode\":\"1\",\"countryCode\":\"57\"}]}"))
                .andExpect(status().isPreconditionFailed())
                .andExpect(jsonPath("$.errorMessage", is("El usuario ya existe.")));
    }
}