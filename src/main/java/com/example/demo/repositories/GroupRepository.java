package com.example.demo.repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.entities.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;


public interface GroupRepository extends CrudRepository<Group, Long> {

    public List<Group> findAll();

    public Optional<Group> findById(Long id);

    public List<Group> findByUsersId(Long id);

    public List<Group> findByAdmin(User user);

    public List<Group> findByDate(Date date);

    Optional<Group> findByName(String name);

    public List<Group> findByDateAndName(Date date, String name);

    boolean existsByName(String name);

}

