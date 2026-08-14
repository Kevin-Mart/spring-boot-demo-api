package com.practica.demo.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

@Service
public class EncryptionService {
    
    private static final String TRANSFORMATION = "AES/CBC/PKCS5padding";

    private final SecretKeySpec secretKeySpec;
    private final IvParameterSpec ivParameterSpec;

    public EncryptionService(SecretKeySpec secretKeySpec ,IvParameterSpec ivParameterSpec){
        this.secretKeySpec = secretKeySpec;
        this.ivParameterSpec = ivParameterSpec;
    }

    public String encrypt(String normalText) {
        try{
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

            byte[] textCifradoBytes = cipher.doFinal(normalText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(textCifradoBytes);
        }catch (Exception e) {
            throw new RuntimeException("error to information cifrado", e);
        }
    }

    public String decrypt(String textCifrado){

        try{
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

            byte[] textCifradoBytes = Base64.getDecoder().decode(textCifrado);
            byte[] normalTextBytes = cipher.doFinal(textCifradoBytes);

            return new String (normalTextBytes, StandardCharsets.UTF_8);
        }catch (Exception e){
            throw new RuntimeException("error to decipher the information", e);
        }
    }


}
