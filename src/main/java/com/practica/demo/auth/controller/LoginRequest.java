package com.practica.demo.auth.controller;


public record LoginRequest(
    String email,
    String password
) {
} 
