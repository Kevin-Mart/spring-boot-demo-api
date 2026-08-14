package com.practica.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI CustomOpenApi(){

        final String securitySchemaName = "bearerAuth";

        return new OpenAPI()
            .info(new Info()
                .title("Demo Api")
                .description("Documentacion de los servicios de usuarios, PokeApi y Cifrado AES")
                .version("1.0.0"))
            .addSecurityItem(new SecurityRequirement().addList(securitySchemaName))
            .components(new Components()
                .addSecuritySchemes(securitySchemaName, 
                    new SecurityScheme()
                        .name(securitySchemaName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")));
    } 
}
