package com.nissum.security.service;

import com.nissum.security.domain.dto.UserRequestDTO;
import com.nissum.security.domain.dto.UserResponseDTO;
import com.nissum.security.domain.exceptions.NissumException;
import com.nissum.security.mapper.UserMapper;
import com.nissum.security.persistence.UserRepository;
import com.nissum.security.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.regex.Pattern;

import static com.nissum.security.domain.enums.NissumExceptionsTypes.USER_ALREADY_EXIST;
import static java.util.Collections.emptyList;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String USERNAME_REGEX = "^(.*)@";
    private final UserRepository userRepository;

    private final JwtTokenProvider tokenProvider;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(usr -> new User(usr.getUsername(), usr.getPassword(), emptyList()))
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
        Authentication auth = new UsernamePasswordAuthenticationToken(username, passwordEncoded);
        final var token = tokenProvider.generateToken(auth);
        var userEntity = userMapper.toEntity(userDto, username, passwordEncoded, token, LocalDateTime.now(), new HashSet<>());
        try {
            var userSaved = userRepository.save(userEntity);
            return userMapper.toDto(userSaved);
        } catch (DataIntegrityViolationException ex) {
            throw new NissumException(String.format("Usuario ya existe con %s", userDto.email()), USER_ALREADY_EXIST);
        }
    }
}
