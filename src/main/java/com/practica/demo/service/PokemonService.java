package com.practica.demo.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import com.practica.demo.dto.PokemonResponse;
import com.practica.demo.dto.pokeapi.PokemonApiResponse;

@Service
public class PokemonService {

    private final RestClient pokeApiClient;

    public PokemonService(RestClient pokeApiClient) {
        this.pokeApiClient = pokeApiClient;
    }

    public PokemonResponse findByName(String name) {
        PokemonApiResponse api = pokeApiClient.get()
                .uri("/pokemon/{name}", name.toLowerCase())
                .retrieve()
                .onStatus(status -> status.value() == 404, (request, reponse) ->{
                    throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Pokemon: '" + name + "' Not found."
                    );
                })
                .body(PokemonApiResponse.class);

        List<String> tipos = api.types().stream()
                .map(t -> t.type().name())
                .toList();

        return new PokemonResponse(
                api.name(),
                api.weight(),
                api.height(),
                tipos
        );
    }
}