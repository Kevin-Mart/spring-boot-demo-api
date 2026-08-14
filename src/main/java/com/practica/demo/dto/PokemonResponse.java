package com.practica.demo.dto;

import java.util.List;

public record PokemonResponse(
    String name,
    int weight,
    int height,
    List<String> type
) {} 