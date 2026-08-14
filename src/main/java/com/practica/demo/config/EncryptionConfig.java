package com.practica.demo.config;

import java.nio.charset.StandardCharsets;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class EncryptionConfig {

    @Value("${app.encryption.secret-key}")
    private String secretKey;

    @Value("${app.encryption.iv}")
    private String iv;

    @Bean
    public SecretKeySpec secretKeySpec(){
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(keyBytes, "AES");
    }

    @Bean
    public IvParameterSpec ivParameterSpec(){
        byte[] ivBytes = iv.getBytes(StandardCharsets.UTF_8);
        return new IvParameterSpec(ivBytes);
    }

}
