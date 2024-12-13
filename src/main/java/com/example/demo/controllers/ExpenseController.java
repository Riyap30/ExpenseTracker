package com.example.demo.controllers;

import java.util.List;

import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTOs.CategoryExpenseDTO;
import com.example.demo.DTOs.ExpenseDTO;
import com.example.demo.entities.*;
import com.example.demo.graphs.BarGraphTotal;
import com.example.demo.graphs.PieChartTotal;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.repositories.ExpenseRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.ExpenseServices;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {
    
    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private ExpenseServices expenseServices;


    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    // Create Expense
    @PostMapping(path = "/", consumes = "application/json")
    public ResponseEntity<ExpenseDTO> createExpense(@RequestBody ExpenseDTO expense) {
        System.out.println(expense);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = ((User) principal).getId();
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found with id " + userId));

        Category category = categoryRepository.findByName(expense.categoryName()).orElseThrow(() -> new IllegalArgumentException("Category not found with name " + expense.categoryName()));
        Expense newExpense = new Expense();
        newExpense.setAmount(expense.amount());
        newExpense.setDescription(expense.description());
        newExpense.setDate(expense.date());
        newExpense.setUser(user);
        newExpense.setCategory(category);
        System.out.println("here");
        expenseRepository.save(newExpense);
        ExpenseDTO expenseDTO = new ExpenseDTO(newExpense.getId(), newExpense.getAmount(), newExpense.getDescription(), newExpense.getDate(), newExpense.getUser().getId(), newExpense.getCategory().getName());
        System.out.println(expenseDTO);
        return ResponseEntity.ok(expenseDTO);
    }

    // Get All Expenses
    @GetMapping("/all")
    public ResponseEntity<List<ExpenseDTO>> getAllExpenses() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = ((User) principal).getId();
        List<ExpenseDTO> expenses = expenseServices.findExpensesByUserId(userId);
        return ResponseEntity.ok(expenses);
    }

    // get specific Expense detail
    @GetMapping("/{expenseId}")
    public ResponseEntity<ExpenseDTO> getExpenseById(@PathVariable Long expenseId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = ((User) principal).getId();
        Optional<ExpenseDTO> optionalExpense = expenseServices.findExpenseById(expenseId);
        if (optionalExpense.get().userId() == userId && optionalExpense.isPresent()) {
            return ResponseEntity.ok(optionalExpense.get());
        } else{
            return ResponseEntity.notFound().build(); // 404
        }
    }

    // Update Expense
    @PutMapping("/{expenseId}")
    public ResponseEntity<ExpenseDTO> updateExpense(@PathVariable Long expenseId, @RequestBody ExpenseDTO expenseDetails) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = ((User) principal).getId();
        Expense expense = expenseRepository.findById(expenseId).orElseThrow(() -> new IllegalArgumentException("Expense not found with id " + expenseId));
        // Check if the user making the request is the owner of the expense
        if (expense.getUser().getId() != userId) {
            return ResponseEntity.status(401).build();
        }
        Expense updatedExpense = expenseServices.updateExpense(expenseId, expenseDetails);
        
        ExpenseDTO expenseDTO = new ExpenseDTO(updatedExpense.getId(), updatedExpense.getAmount(), updatedExpense.getDescription(), updatedExpense.getDate(), updatedExpense.getUser().getId(), updatedExpense.getCategory().getName());
        return ResponseEntity.ok(expenseDTO);
    }

    // Delete Expense
    @DeleteMapping("/{expenseId}")
    public ResponseEntity<Object> deleteExpense(@PathVariable Long expenseId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = ((User) principal).getId();
        Expense expense = expenseRepository.findById(expenseId).orElseThrow(() -> new IllegalArgumentException("Expense not found with id " + expenseId));
        // Check if the user making the request is the owner of the expense
        if (expense.getUser().getId() != userId) {
            return ResponseEntity.status(401).build();
        }
        return expenseServices.deleteExpense(expenseId);
    }

    // Get Expenses by Category
    @GetMapping("/{category}")
    public ResponseEntity<List<ExpenseDTO>> getExpenseByCategory(@PathVariable Long category) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = ((User) principal).getId();
        // check if user and category exist
        Optional<User> user = userRepository.findById(userId);
        
        if (!user.isPresent()) {
            throw new IllegalArgumentException("User not found with id " + userId);
        }

        Optional<Category> categoryOpt = categoryRepository.findById(category);
        if (!categoryOpt.isPresent()) {
            throw new IllegalArgumentException("Category not found with name " + category);
        }
        List<ExpenseDTO> expenses = expenseServices.findExpensesByCategoryAndUser(categoryOpt.get(), user.get());
        return ResponseEntity.ok(expenses);

    }

    // Get total expenses per category for bar chart
    @GetMapping("/category-total/bar-graph")
    public ResponseEntity<String> getExpensesByCategoryBar() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = ((User) principal).getId();
        List<CategoryExpenseDTO> expenses = expenseServices.getTotalExpensesByCategory(userId);
        BarGraphTotal barGraphTotal = new BarGraphTotal();
        String image = barGraphTotal.saveChartAsImage(expenses);
        return ResponseEntity.ok(image);
    }

    // Get total expenses per category for pie chart
    @GetMapping("/category-total/pie-chart")
    public ResponseEntity<String> getExpensesByCategoryPie() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = ((User) principal).getId();
        List<CategoryExpenseDTO> expenses = expenseServices.getTotalExpensesByCategory(userId);
        PieChartTotal pieChartTotal = new PieChartTotal();
        String image = pieChartTotal.saveChartAsImage(expenses);
        return ResponseEntity.ok(image);
    }
}
