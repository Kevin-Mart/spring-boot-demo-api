package com.practica.demo.config;

import org.springframework.http.HttpHeaders;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.practica.demo.auth.repository.Token;
import com.practica.demo.auth.repository.TokenRepository;

import io.jsonwebtoken.Jwt;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {
    private final AuthenticationProvider authenticationProvider;
    private final TokenRepository tokenRepository;

    private final JwtDecoder jwtDecoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
            .csrf(AbstractHttpConfigurer:: disable)
            .authorizeHttpRequests(req ->
                req.requestMatchers(
                    "/auth/**",
                    "/error",
                    "/h2-console/**",
                    "/",
                    "/*.html",
                    "/css/**",
                    "/js/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/v3/api-docs/**"
                )
                .permitAll()
                .anyRequest()
                .authenticated()
            )// 1. Deshabilitar X-Frame-Options para permitir los frames de H2
            .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.sameOrigin())
                // 2. Permitir scripts e imágenes dentro de la misma interfaz de H2 (evita bloqueos CSP)
                .contentSecurityPolicy(csp -> csp
                    .policyDirectives("script-src 'self' 'unsafe-inline'; object-src 'none';")
                )
            ).sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider)
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    .decoder(jwtDecoder)
                    .jwtAuthenticationConverter(jwtAuthenticationConverter()))
            )
            .logout(logout ->
                logout.logoutUrl("/auth/logout")
                    .addLogoutHandler((request, response, authentication) -> {
                        final var authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
                        revocadoToken(authHeader);
                    })
                    .logoutSuccessHandler((request, response, authentication) -> 
                        SecurityContextHolder.clearContext())
            );
        return http.build();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthoritiesClaimName("scope");
        authoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return converter;
    }

    private void revocadoToken(final String authHeader){
        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            throw new IllegalArgumentException("Token inválido");
        }
        final String jwt = authHeader.substring(7);
        final var token = tokenRepository.findByToken(jwt)
            .orElseThrow(() -> new IllegalArgumentException("Token inválido"));
        token.setExpired(true);
        token.setRevoked(true);
        tokenRepository.save(token);
    }

}
