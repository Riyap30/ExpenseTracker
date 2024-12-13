package com.example.demo.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.demo.entities.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends CrudRepository<Expense, Long> {

    public List<Expense> findAll();

    public Optional<Expense> findById(Long id);

    public List<Expense> findByUserId(Long id);
    
    public Double findTotalExpenseByCategory(@Param("categoryId") long categoryId);  

    
    public List<Expense> findByUser(User user);

    public List<Expense> findByCategoryAndUser(Category category, User user);

    public List<Expense> findByUserAndCategory(Optional<User> user, Optional<Category> categoryOpt);

    public List<Expense> findByUserAndDate(User user, Date date);

    public List<Expense> findByUserAndDateAndCategory(User user, Date date, Category category);
}
