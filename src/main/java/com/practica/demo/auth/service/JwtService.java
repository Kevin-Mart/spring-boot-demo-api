package com.practica.demo.auth.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;


import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.practica.demo.usuario.User;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {


    private final JwtDecoder jwtDecoder;
    private final JwtEncoder jwtEncoder;

    public String generateAccesToken(User user){
        return buildToken(user,1, ChronoUnit.HOURS, "ACCESS");
    }

    public String generateRefreshToken(User user){
        return buildToken(user,7, ChronoUnit.DAYS, "REFESH");
    }

    private String buildToken(User user, long amount, ChronoUnit unit, String type){
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("demo-app")
                .issuedAt(now)
                .expiresAt(now.plus(amount,unit))
                .subject(user.getEmail())
                .claim("userId", user.getId())
                .claim("name", user.getName())
                .claim("scope","USER")
                .claim("token_type",type)
                .build();
        JwsHeader header = JwsHeader.with(SignatureAlgorithm.RS256).build();

        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    public String extractUsername(final String token) {
        return jwtDecoder.decode(token).getSubject();
    }
}
