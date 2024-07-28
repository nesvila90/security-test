package com.nissum.security.security.jwt;

import com.nissum.security.security.config.TokenConfig;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.security.Key;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@TestPropertySource(locations = "classpath*:test.properties", properties = {"jwt.expiration.date=6000L", "jwt.secret= mysecretkeymysecretkeymysecretkeymysecretkey"})
class JwtTokenProviderTest {

    @InjectMocks
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private TokenConfig tokenConfig;

    @Mock
    private Authentication authentication;

    private Long expirationTime = 3600000L;  // 1 hour in milliseconds
    private String secretKey = "125346546578fdsdmfksfksmgdkmslmvlmsflmdlfsprjmetphmpodofdkgol3213213dwlsd"; // Needs to be 32 chars
    private Key key;

    @BeforeEach
    void setUp() {
        // Mocking the TokenConfig properties
        when(tokenConfig.getExpirationTime()).thenReturn(expirationTime);
        when(tokenConfig.getSecretKey()).thenReturn(secretKey);

        // Initializing the key
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        when(tokenConfig.getKey()).thenReturn(key);

        // Inject the mocked TokenConfig into JwtTokenProvider
        jwtTokenProvider = new JwtTokenProvider(tokenConfig);
    }

    @Test
    void testGenerateToken() {
        when(authentication.getPrincipal()).thenReturn("user");

        String token = jwtTokenProvider.generateToken(authentication);
        assertNotNull(token);

        String userId = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
        assertEquals("user", userId);
    }

    @Test
    void testGetUserIdFromJWT() {
        String token = Jwts.builder()
                .setSubject("user")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key)
                .compact();

        String userId = jwtTokenProvider.getUserIdFromJWT(token);
        assertEquals("user", userId);
    }

    @Test
    void testValidateToken_ValidToken() {
        String token = Jwts.builder()
                .setSubject("user")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key)
                .compact();

        assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void testValidateToken_InvalidToken() {
        String invalidToken = "invalidToken";
        assertFalse(jwtTokenProvider.validateToken(invalidToken));
    }

    @Test
    void testValidateToken_ExpiredToken() {
        String expiredToken = Jwts.builder()
                .setSubject("user")
                .setIssuedAt(new Date(System.currentTimeMillis() - expirationTime - 1000))  // Token expired 1 second ago
                .setExpiration(new Date(System.currentTimeMillis() - 1000))  // Expired 1 second ago
                .signWith(key)
                .compact();

        assertFalse(jwtTokenProvider.validateToken(expiredToken));
    }
}
