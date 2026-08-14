package com.practica.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.practica.demo.dto.PokemonResponse;
import com.practica.demo.service.PokemonService;

import io.swagger.v3.oas.annotations.tags.Tag;
@Tag(name = "Pokemon", description = "Consultar datos de la PokeApi")
@RestController
@RequestMapping("/api/pokemon")
public class PokemonController {

    private final PokemonService pokemonService;

    public PokemonController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    @GetMapping("/{name}")
    public PokemonResponse buscar(@PathVariable String name) {
        return pokemonService.findByName(name);
    }
}