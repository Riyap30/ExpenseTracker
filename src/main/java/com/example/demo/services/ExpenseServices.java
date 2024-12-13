package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.repositories.CategoryRepository;
import com.example.demo.repositories.ExpenseRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.DTOs.CategoryExpenseDTO;
import com.example.demo.DTOs.ExpenseDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.demo.entities.*;


@Service
public class ExpenseServices {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private CategoryServices categoryServices;

    @Autowired
    private CategoryRepository categoryRepository;

    
    // find all expenses
    public List<ExpenseDTO> findAllExpenses() {
        List<ExpenseDTO> expenses = expenseRepository.findAll().stream().map(
            expense -> new ExpenseDTO(expense.getId(), expense.getAmount(), expense.getDescription(), expense.getDate(), expense.getUser().getId(), expense.getCategory().getName())
        ).toList();
        return expenses;
    }

    // find expenses by user id
    public List<ExpenseDTO> findExpensesByUserId(long userId){
        List<ExpenseDTO> expenses = expenseRepository.findByUserId(userId).stream().map(
            expense -> new ExpenseDTO(expense.getId(), expense.getAmount(), expense.getDescription(), expense.getDate(), expense.getUser().getId(), expense.getCategory().getName())
        ).toList();
        return expenses;
    }

    // find expense by id
    public Optional<ExpenseDTO> findExpenseById(long id){
        Optional<ExpenseDTO> expenseFound = expenseRepository.findById(id).map(
            expense -> new ExpenseDTO(expense.getId(), expense.getAmount(), expense.getDescription(), expense.getDate(), expense.getUser().getId(), expense.getCategory().getName())
        );
        return expenseFound;
    }

    // update expense
    public Expense updateExpense(long expenseId, ExpenseDTO expenseDTO) {
        Expense expense = expenseRepository.findById(expenseId).orElseThrow(() -> new IllegalArgumentException("Expense not found with id " + expenseId));
        expense.setAmount(expenseDTO.amount());
        expense.setDescription(expenseDTO.description());
        expense.setDate(expenseDTO.date());
        Optional<Category> category = categoryServices.findCategoryByName(expenseDTO.categoryName());
        expense.setCategory(category.get());
        Optional<User> user = userRepository.findById(expenseDTO.userId());
        expense.setUser(user.get());
        return expenseRepository.save(expense);
        
    }

    // find expense by category and user
    public List<ExpenseDTO> findExpensesByCategoryAndUser(Category category, User user){
        List<ExpenseDTO> expenses = expenseRepository.findByCategoryAndUser(category, user).stream().map(
            expense -> new ExpenseDTO(expense.getId(), expense.getAmount(), expense.getDescription(), expense.getDate(), expense.getUser().getId(), expense.getCategory().getName())
        ).toList();
        return expenses;

        
    }


    // get total expenses by category
    public List<CategoryExpenseDTO> getTotalExpensesByCategory(long userId) {
        List<Expense> expenses = expenseRepository.findByUserId(userId);
        List<Category> categories = categoryRepository.findAll();

        List<CategoryExpenseDTO> categoryExpenseDTOs = new ArrayList<>();

        for (Category category : categories) {
            List<Expense> categoryExpenses = expenses.stream().filter(expense -> expense.getCategory().getName().equals(category.getName())).toList();
            double total = 0;
            for (Expense expense : categoryExpenses) {
                total += expense.getAmount();
            }
            CategoryExpenseDTO categoryExpenseDTO = new CategoryExpenseDTO(category.getName(), total);
            categoryExpenseDTOs.add(categoryExpenseDTO);
        }
        return categoryExpenseDTOs;
    }

    // save expense
    public Expense saveExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    // Delete expense by id
    public ResponseEntity<Object> deleteExpense(long id) {
        expenseRepository.deleteById(id);
        return null;

    }
    
}
