package com.example.roenbeauty.global.security;

import com.example.roenbeauty.global.dto.AuthUser;
import com.example.roenbeauty.user.enums.UserRole;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    public JwtAuthenticationFilter(
            JwtProvider jwtProvider
    ) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authorizationHeader =
                request.getHeader("Authorization");

        if (authorizationHeader != null
                && authorizationHeader.startsWith("Bearer ")) {

            String token =
                    authorizationHeader.substring(7);

            try {

                Claims claims =
                        jwtProvider.extractClaims(token);

                Long userId =
                        Long.valueOf(claims.getSubject());

                String email =
                        claims.get("email", String.class);

                UserRole role =
                        UserRole.of(
                                claims.get("role", String.class)
                        );

                AuthUser authUser =
                        new AuthUser(
                                userId,
                                email,
                                role
                        );

                JwtAuthenticationToken authentication =
                        new JwtAuthenticationToken(authUser);

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);

            } catch (Exception ignored) {
            }
        }

        filterChain.doFilter(request, response);
    }
}