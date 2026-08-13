package com.practica.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NameResponse(
    @JsonProperty("name") String name
) {
}
