package com.lichius.rac.ansbach.altstadtfest.infrastructure.filter;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Einfacher JWT-Filter, der nur die Admin-Endpunkte absichert:
 * - PATCH /api/products/{id}
 * - POST  /api/products/reset
 *
 * Alle anderen Requests werden durchgelassen.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final SecretKey signingKey;

    public JwtAuthFilter(@Value("${app.jwt-secret}") String jwtSecret) {
        this.signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String method = request.getMethod();
        String path = request.getRequestURI();

        // CORS-Preflight immer durchlassen
        if ("OPTIONS".equalsIgnoreCase(method)) {
            return true;
        }

        // Nur Admin-Endpunkte filtern
        boolean isProductPatch = "PATCH".equalsIgnoreCase(method) && path.matches("/api/products/\\d+");
        boolean isProductReset = "POST".equalsIgnoreCase(method) && "/api/products/reset".equals(path);

        // shouldNotFilter = true → Request wird NICHT gefiltert (= durchgelassen)
        return !(isProductPatch || isProductReset);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"Token fehlt\"}");
            response.setContentType("application/json");
            return;
        }

        String token = authHeader.substring(7);

        try {
            Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(token);
        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"Token ungültig oder abgelaufen\"}");
            response.setContentType("application/json");
            return;
        }

        filterChain.doFilter(request, response);
    }
}


