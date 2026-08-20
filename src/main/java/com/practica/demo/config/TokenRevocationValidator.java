package com.practica.demo.config;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import com.practica.demo.auth.repository.TokenRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenRevocationValidator implements OAuth2TokenValidator<Jwt> {

    private final TokenRepository tokenRepository;

    @Override
    public OAuth2TokenValidatorResult validate (Jwt token){
        return tokenRepository.findByToken(token.getTokenValue())
                .filter(t -> !t.isRevoked() && !t.isExpired())
                .map(t-> OAuth2TokenValidatorResult.success())
                .orElseGet(() -> OAuth2TokenValidatorResult.failure(
                    new OAuth2Error("invalid_token", "Token revocado o no encontrado", null)));
    }

}
