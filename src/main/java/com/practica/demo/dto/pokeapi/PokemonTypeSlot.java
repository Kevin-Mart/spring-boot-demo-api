package com.practica.demo.dto.pokeapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PokemonTypeSlot(
    int slot,
    PokemonType type
) {}