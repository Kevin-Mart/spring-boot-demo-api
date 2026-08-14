package com.practica.demo.dto.pokeapi;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PokemonApiResponse(
    String name,
    int height,
    int weight,
    List<PokemonTypeSlot> types) {
}
