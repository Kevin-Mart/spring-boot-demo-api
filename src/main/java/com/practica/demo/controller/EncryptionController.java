package com.practica.demo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.practica.demo.dto.encryption.DecryptRequest;
import com.practica.demo.dto.encryption.DecryptResponse;
import com.practica.demo.dto.encryption.EncryptRequest;
import com.practica.demo.dto.encryption.EncryptResponse;
import com.practica.demo.service.EncryptionService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Encryptacion AES/CBC/PKCS5Padding", description = "Servicio de incryptacion de cualquier longitud")
@RestController
@RequestMapping("/api/encryption")
public class EncryptionController {

    private final EncryptionService encryptionService;

    public EncryptionController (EncryptionService encryptionService){
        this.encryptionService = encryptionService;
    }

    @PostMapping("/encrypt")
    public EncryptResponse encrypt(@RequestBody EncryptRequest request){
        String result = encryptionService.encrypt(request.text());
        return new EncryptResponse(result);
    }

    @PostMapping("/decrypt")
    public DecryptResponse decrypt (@RequestBody DecryptRequest request){
        String result = encryptionService.decrypt(request.textCifrado());
        return new DecryptResponse(result);
    }

}
