package com.practica.demo.bitacora;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bitacora")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bitacora {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "app_user")
    private String user;

    private String httpMethod;
    private String endpoint;
    private String executedMethod;
    private String parameters;
    private String result;
    private String errorMessage;
    private Long durationMs;

    @Column(name = "created_at")
    private LocalDateTime timestamp;
}