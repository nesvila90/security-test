package com.nissum.security.service;

import com.nissum.security.domain.dto.UserAuthenticatedResponseDTO;
import com.nissum.security.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public UserAuthenticatedResponseDTO authenticateUser(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        var jwt = jwtTokenProvider.generateToken(authentication);
        var principal = (UserDetailsImpl) authentication.getPrincipal();
        return new UserAuthenticatedResponseDTO(jwt, principal.getId(), principal.getUsername(), principal.getEmail(), new ArrayList<>());
    }
}
