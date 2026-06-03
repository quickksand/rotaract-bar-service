package com.lichius.rac.ansbach.altstadtfest.application.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import rotaract.bar.infrastructure.api.controller.AuthControllerApi;
import rotaract.bar.infrastructure.api.controller.model.LoginRequestDto;
import rotaract.bar.infrastructure.api.controller.model.LoginResponseDto;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@RestController
public class AuthController implements AuthControllerApi {

    private final String adminPin;
    private final SecretKey signingKey;

    public AuthController(
            @Value("${app.admin-pin}") String adminPin,
            @Value("${app.jwt-secret}") String jwtSecret
    ) {
        this.adminPin = adminPin;
        this.signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public ResponseEntity<LoginResponseDto> login(LoginRequestDto loginRequestDto) {
        if (!adminPin.equals(loginRequestDto.getPin())) {
            return ResponseEntity.status(401).build();
        }

        Instant now = Instant.now();
        String token = Jwts.builder()
                .subject("admin")
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(8, ChronoUnit.HOURS)))
                .signWith(signingKey)
                .compact();

        LoginResponseDto response = new LoginResponseDto();
        response.setToken(token);
        return ResponseEntity.ok(response);
    }
}

