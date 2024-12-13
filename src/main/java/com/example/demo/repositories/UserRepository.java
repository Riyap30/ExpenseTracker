package com.example.demo.repositories;


import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import com.example.demo.entities.User;


public interface UserRepository extends CrudRepository<User, Long> {

    public List<User> findAll();

    public Optional<User> findById(Long id);

    public Optional<User> findByUsername(String username);

    public Optional<User> findByEmail(String email);

    public List<User> findByUsernameContainingIgnoreCase(String query);
}
