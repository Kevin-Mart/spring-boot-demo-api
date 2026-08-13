package com.practica.demo.auth.controller;

public record RegisterRequest (
    String email,
    String password,
    String name
){

}
