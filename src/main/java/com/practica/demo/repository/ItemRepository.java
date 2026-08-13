package com.practica.demo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.practica.demo.dto.NameOnly;
import com.practica.demo.usuario.User;

public interface ItemRepository extends JpaRepository<User,Long> {

    List<NameOnly> findDistinctByNameContaining(String name);
    
    List<NameOnly> findBy();
}
