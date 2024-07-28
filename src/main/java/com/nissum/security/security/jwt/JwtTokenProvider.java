package com.nissum.security.security.jwt;

import com.nissum.security.security.config.TokenConfig;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final TokenConfig tokenConfig;

    public String generateToken(Authentication authentication) {
        var userPrincipal = (String) authentication.getPrincipal();

        final var expirationDate = getExpirationDate(tokenConfig.getExpirationTime());
        return Jwts.builder()
                .setSubject(userPrincipal)
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(tokenConfig.getKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    private Date getExpirationDate(Long expirationTime) {
        return Date.from(LocalDateTime.now()
                .plus(expirationTime, ChronoUnit.MILLIS)
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    public String getUserIdFromJWT(String token) {
        return Jwts.parserBuilder().setSigningKey(tokenConfig.getKey()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(tokenConfig.getKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
