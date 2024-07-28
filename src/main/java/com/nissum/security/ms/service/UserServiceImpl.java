package com.nissum.security.ms.service;

import com.nissum.security.ms.domain.dto.UserRequestDTO;
import com.nissum.security.ms.domain.dto.UserResponseDTO;
import com.nissum.security.ms.domain.entities.UserEntity;
import com.nissum.security.ms.mapper.UserMapper;
import com.nissum.security.ms.persistence.UserRepository;
import com.nissum.security.ms.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String USERNAME_REGEX = "^(.*)@";
    private final UserRepository userRepository;

    private final TokenProvider tokenProvider;

    private final BCryptPasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    //private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(usr -> new User(usr.getUsername(), usr.getPassword(),
                        Collections.emptyList()
                ))
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found"));
    }

    public UserResponseDTO createUser(UserRequestDTO userDto) {
        var pattern = Pattern.compile(USERNAME_REGEX);
        var matcher = pattern.matcher(userDto.email());

        String username = null;
        if (matcher.find()) {
            username = matcher.group(1);
        }

        var passwordEncoded = passwordEncoder.encode(userDto.password());
        final var token = tokenProvider.generateToken(username);
        var userEntity = userMapper.toEntity(userDto, username, passwordEncoded, token, LocalDateTime.now(), new HashSet<>());
        var userSaved = userRepository.save(userEntity);
        return userMapper.toDto(userSaved);
    }
}
