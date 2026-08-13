package com.practica.demo.entity;

import com.practica.demo.usuario.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "compra")
public class Compra {

    @Id
    @GeneratedValue
    Long id;

    String itemCompra;
    Long precio;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


}
