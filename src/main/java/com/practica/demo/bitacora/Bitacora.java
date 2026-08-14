package com.practica.demo.bitacora;

import java.time.LocalDateTime;

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

    private String usuario;
    private String metodoHttp;
    private String endpoint;
    private String metodoEjecutado;
    private String parametros;
    private String resultado;
    private String mensajeError;
    private Long duracionMs;
    private LocalDateTime fecha;
}