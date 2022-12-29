package com.bezkoder.springjwt.security.jwt;

import com.bezkoder.springjwt.security.services.UserDetailsImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;


@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${bezkoder.app.jwtSecret}")
    private String jwtSecret;

    @Value("${bezkoder.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        Instant now = Instant.now();

        Date issuedAt = new Date(now.toEpochMilli());
        Date exp = new Date(now.plus(jwtExpirationMs, ChronoUnit.MILLIS).toEpochMilli());

        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(issuedAt)
                .setExpiration(exp)
                .signWith(key)
                .compact();
    }

    public String validateJwtTokenAndExtractUsername(String jwt) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        Jws<Claims> jws = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt);
        return jws.getBody().getSubject();
    }
}
