package com.example.demo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.entities.*;


public interface SplitRepository extends CrudRepository<Split, Long> {

    public List<Split> findAll();
    public List<Split> findByGroup(Group group);

    public List<Split> findByGroupId(long id);

    public Optional<Split> findByPayerIdAndPayeeId(long id, long id2);
    
    public List<Split> findByPayerOrPayee(User user, User user2);

    public Optional<Split> findById(Long id);

    public List<Split> findByPayerIdOrPayeeId(long id, long id2);

}
