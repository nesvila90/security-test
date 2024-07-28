package com.nissum.security.service;

import com.nissum.security.domain.dto.UserAuthenticatedResponseDTO;

public interface AuthenticationService {


    UserAuthenticatedResponseDTO authenticateUser(String username, String password);
}
