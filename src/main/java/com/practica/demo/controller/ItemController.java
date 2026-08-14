package com.practica.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.practica.demo.dto.ItemFilterRequest;
import com.practica.demo.dto.NameResponse;
import com.practica.demo.service.ItemService;

import io.swagger.v3.oas.annotations.tags.Tag;
@Tag(name = "Filtrado de items", description = "Filtrar todos lo usuarios o por nombre")
@RestController
@RequestMapping("/api/items")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService){
        this.itemService = itemService;
    }

    @GetMapping
    public List<NameResponse> listar(@RequestParam(required = false) String name){
        return itemService.findByname(name);
    }

    @PostMapping("/search")
    public List<NameResponse> search(@RequestBody ItemFilterRequest filter){
        return itemService.findByname(filter.name());
    }
}
