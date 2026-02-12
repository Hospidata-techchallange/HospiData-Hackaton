package br.com.hospidata.auth_service.config;

import br.com.hospidata.auth_service.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.Collections;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final TokenService tokenService;

    public SecurityConfig(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/api/v1/user/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()
//                        .requestMatchers("/actuator/prometheus").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        new JwtAuthFilter(tokenService),
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    static class JwtAuthFilter extends OncePerRequestFilter {

        private final TokenService tokenService;

        public JwtAuthFilter(TokenService tokenService) {
            this.tokenService = tokenService;
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws java.io.IOException, jakarta.servlet.ServletException {

            String token = null;
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if ("accessToken".equals(cookie.getName())) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }

            if (token != null && tokenService.validateToken(token)) {
                String username = tokenService.getUsernameFromToken(token);
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }

            filterChain.doFilter(request, response);
        }
    }
}
