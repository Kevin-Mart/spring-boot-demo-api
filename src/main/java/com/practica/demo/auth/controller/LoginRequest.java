package com.practica.demo.auth.controller;

/**
 * LoginRequest
 */
public record LoginRequest(
    String email,
    String password
) {
} 
