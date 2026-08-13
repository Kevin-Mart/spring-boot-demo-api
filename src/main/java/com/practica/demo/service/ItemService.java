package com.practica.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.practica.demo.dto.NameOnly;
import com.practica.demo.dto.NameResponse;
import com.practica.demo.repository.ItemRepository;
import com.practica.demo.usuario.User;

@Service
public class ItemService {
    private final ItemRepository itemRepository;

    public ItemService( ItemRepository itemRepository ){
        this.itemRepository = itemRepository;
    }

    public List<NameResponse> findByname(String name){
        List<NameOnly> names;
        if(name == null || name.isBlank()){
            names = itemRepository.findBy();
        }else{
            names = itemRepository.findDistinctByNameContaining(name);    
        }
        return names.stream()
            .map(n -> new NameResponse(n.getName()))
            .collect(Collectors.toList());
    }
}
