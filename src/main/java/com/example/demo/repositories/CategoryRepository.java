package com.example.demo.repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.entities.*;

import java.util.List;
import java.util.Optional;



public interface CategoryRepository extends CrudRepository<Category, Long> {

    public List<Category> findAll();

    public Optional<Category> findByName(String name);


}
