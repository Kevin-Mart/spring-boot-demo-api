package com.practica.demo.auth.service;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.practica.demo.usuario.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${app.jwt.secret-key}")
    private String secreteKey;
    @Value("${app.jwt.expiration-minutes}")
    private long jwtExpiration;
    @Value("${app.jwt.refresh-token.expiration}")
    private long refreshExpiration;


    public String extractUsername(final String token){
        final Claims jwtToken = Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return jwtToken.getSubject();
    }

    public String generateToken(final User user){
        return buildToken(user,jwtExpiration);
    }

    public String generateRefreshToken(final User user){
        return buildToken(user,refreshExpiration);
    }

    public String buildToken(final User user, final long expiration){
        return Jwts.builder()
        .id(user.getId().toString())
        .claims(Map.of("name", user.getName()))
        .subject(user.getEmail())
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date (System.currentTimeMillis()+ expiration*60_000))
        .signWith(getSignInKey())
        .compact();
    }

    public boolean isTokenValid(final String token,final User user){
        final String username= extractUsername(token);
        return(username.equals(user.getEmail()) && !isTokenExpired(token));
    }

    public boolean isTokenExpired (final String token){
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration (final String token){
        final Claims jwtToken = Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return jwtToken.getExpiration();
    }



    private SecretKey getSignInKey(){
        byte [] keyBytes = Decoders.BASE64.decode(secreteKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
