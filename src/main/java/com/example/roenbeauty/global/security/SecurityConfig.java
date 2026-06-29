package com.example.roenbeauty.global.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter
    ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        SecurityContextHolderAwareRequestFilter.class
                )

                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(auth -> auth

                        // 정적 페이지, 정적 리소스
                        .requestMatchers(
                                "/",
                                "/*.html",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/favicon.ico"
                        ).permitAll()

                        // 회원가입, 로그인
                        .requestMatchers(
                                "/api/users/signup",
                                "/api/users/login",
                                "/api/users/kakao/login"
                        ).permitAll()

                        // 로그인 필요 : 탈퇴
                        .requestMatchers(HttpMethod.DELETE, "/api/users/me").authenticated()

                        // 공개 조회 API
                        .requestMatchers(HttpMethod.GET, "/api/menus").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/menus/category/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/galleries").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/galleries/category/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/business-hours").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/business-hours/holidays").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/reservations/available-times").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/reservation-notice").permitAll()

                        // 로그인 필요: 예약/결제/취소
                        .requestMatchers(HttpMethod.POST, "/api/reservations").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/reservations/my").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/reservations/*/cancel").authenticated()
                        .requestMatchers("/api/reservations/*/payments/**").authenticated()

                        // 관리자 전용: 예약 관리
                        .requestMatchers(HttpMethod.GET, "/api/reservations").hasAnyAuthority("ADMIN", "OWNER")
                        .requestMatchers(HttpMethod.PATCH, "/api/reservations/*/status").hasAnyAuthority("ADMIN", "OWNER")
                        .requestMatchers(HttpMethod.GET, "/api/reservations/reserved-times").hasAnyAuthority("ADMIN", "OWNER")

                        // 관리자 전용: 메뉴 관리
                        .requestMatchers(HttpMethod.GET, "/api/menus/admin").hasAnyAuthority("ADMIN", "OWNER")
                        .requestMatchers(HttpMethod.POST, "/api/menus").hasAnyAuthority("ADMIN", "OWNER")
                        .requestMatchers(HttpMethod.PUT, "/api/menus/*").hasAnyAuthority("ADMIN", "OWNER")

                        // 관리자 전용: 갤러리 관리
                        .requestMatchers(HttpMethod.GET, "/api/galleries/admin").hasAnyAuthority("ADMIN", "OWNER")
                        .requestMatchers(HttpMethod.POST, "/api/galleries").hasAnyAuthority("ADMIN", "OWNER")
                        .requestMatchers(HttpMethod.PUT, "/api/galleries/*").hasAnyAuthority("ADMIN", "OWNER")
                        .requestMatchers(HttpMethod.DELETE, "/api/galleries/*").hasAnyAuthority("ADMIN", "OWNER")

                        // 관리자 전용: 영업시간 관리
                        .requestMatchers(HttpMethod.GET, "/api/business-hours/default").hasAnyAuthority("ADMIN", "OWNER")
                        .requestMatchers(HttpMethod.PUT, "/api/business-hours/default").hasAnyAuthority("ADMIN", "OWNER")
                        .requestMatchers(HttpMethod.GET, "/api/business-hours/exceptions").hasAnyAuthority("ADMIN", "OWNER")
                        .requestMatchers(HttpMethod.POST, "/api/business-hours/exceptions").hasAnyAuthority("ADMIN", "OWNER")
                        
                        // 관리자 전용: 예약 불가 시간 관리
                        .requestMatchers(HttpMethod.GET, "/api/blocked-times").hasAnyAuthority("ADMIN", "OWNER")
                        .requestMatchers(HttpMethod.POST, "/api/blocked-times").hasAnyAuthority("ADMIN", "OWNER")
                        .requestMatchers(HttpMethod.DELETE, "/api/blocked-times/*").hasAnyAuthority("ADMIN", "OWNER")

                        // 관리자 전용: 운영 설정
                        .requestMatchers(HttpMethod.PUT, "/api/reservation-notice").hasAnyAuthority("ADMIN", "OWNER")

                        .anyRequest().permitAll()
                )

                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}