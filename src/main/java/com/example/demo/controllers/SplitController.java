package com.example.demo.controllers;

import java.util.List;
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

import com.example.demo.DTOs.SplitDTO;
import com.example.demo.entities.*;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.repositories.GroupRepository;
import com.example.demo.repositories.SplitRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.SplitServices;

@RestController
@RequestMapping("/splits")
public class SplitController {
    
    @Autowired
    private SplitRepository splitRepository;

    @Autowired
    private SplitServices splitServices;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // Create a split
    @PostMapping("{groupId}")
    public ResponseEntity<SplitDTO> createSplit(@PathVariable Long groupId, @RequestBody SplitDTO splitDetails){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = ((User) principal).getId();
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        // check if user is admin or part of the group
        if (group.getAdmin().getId() != userId && group.getUsers().contains(user) == false) {
            return ResponseEntity.status(401).build();
        }
        // check if the payer and payee are selected
        if (splitDetails.payerId() == splitDetails.payeeId()) {
            return ResponseEntity.status(400).build();
        }
        User payee = userRepository.findById(splitDetails.payeeId()).orElseThrow(() -> new RuntimeException("Payee not found"));
        User payer = userRepository.findById(splitDetails.payerId()).orElseThrow(() -> new RuntimeException("Payer not found"));
        Category category = categoryRepository.findByName(splitDetails.categoryName()).orElseThrow(() -> new RuntimeException("Category not found"));
        Split split = new Split();
        split.setGroup(group);
        split.setPayee(payee);
        split.setPayer(payer);
        split.setCategory(category);
        split.setAmount(splitDetails.amount());
        split.setSettled(false);
        splitServices.saveSplit(split);
        SplitDTO splitDTO = new SplitDTO(split.getId(), split.getAmount(), split.getGroup().getId(), split.getPayer().getId(), split.getPayer().getUsername(), split.getPayee().getId(), split.getPayee().getUsername(), split.getCategory().getName(), split.getSettled(), "");
        return ResponseEntity.ok(splitDTO);
    }

    // Get Split Details
    @GetMapping("/{splitId}")
    public ResponseEntity<SplitDTO> getSplitById(@PathVariable Long splitId){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = ((User) principal).getId();
        Split split = splitRepository.findById(splitId).orElseThrow(() -> new RuntimeException("Split not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        String message = "";
        // check if user is admin or part of the group
        if (split.getGroup().getAdmin().getId() != userId || split.getGroup().getUsers().contains(user) == false) {
            return ResponseEntity.status(401).build();
        }

        if (split.getPayer().getId() != userId && split.getPayee().getId() != userId) {
            message = "You are not involved";
        }

        Split splitFound = splitRepository.findById(splitId).orElseThrow(() -> new RuntimeException("Split not found"));
        
        SplitDTO splitDTO = new SplitDTO(splitFound.getId(), splitFound.getAmount(), splitFound.getGroup().getId(), splitFound.getPayer().getId(), splitFound.getPayer().getUsername(), splitFound.getPayee().getId(), splitFound.getPayee().getUsername(), splitFound.getCategory().getName(), splitFound.getSettled(), message);
        
        return ResponseEntity.ok(splitDTO);
    }
 
    // Update Split Details
    @PutMapping("/{splitId}")
    public ResponseEntity<SplitDTO> updateSplit(@PathVariable Long splitId, @RequestBody SplitDTO splitDetails){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = ((User) principal).getId();
        Split split = splitRepository.findById(splitId).orElseThrow(() -> new RuntimeException("Split not found"));
        User user  = userRepository.findById(splitDetails.payerId()).orElseThrow(() -> new RuntimeException("User not found"));
        // check if user is admin or part of the group
        if (split.getGroup().getAdmin().getId() != userId && split.getGroup().getUsers().contains(user) == false) {
            return ResponseEntity.status(401).build();
        }
        // check if the payer and payee are selected
        if (splitDetails.payerId() == splitDetails.payeeId()) {
            return ResponseEntity.status(400).build();
        }
        // check if the user is part of the split
        if (split.getPayer().getId() != userId && split.getPayee().getId() != userId) {
            return ResponseEntity.status(401).build();
        }
        Split updatedSplit = splitServices.updateSplit(splitId, splitDetails);
        SplitDTO splitDTO = new SplitDTO(updatedSplit.getId(), updatedSplit.getAmount(), updatedSplit.getGroup().getId(), updatedSplit.getPayer().getId(), updatedSplit.getPayer().getUsername(), updatedSplit.getPayee().getId(), updatedSplit.getPayee().getUsername(), updatedSplit.getCategory().getName(), updatedSplit.getSettled(), "");
        return ResponseEntity.ok(splitDTO);
    }

    // Delete Split
    @DeleteMapping("/{splitId}")
    public Object deleteSplit(@PathVariable Long splitId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = ((User) principal).getId();
        Split split = splitRepository.findById(splitId).orElseThrow(() -> new RuntimeException("Split not found"));
        User user  = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        // check if user is admin or part of the group
        if (split.getPayer().getId() != userId && split.getPayee().getId() != userId) {
            return ResponseEntity.status(400).build();
        }
        if (split.getGroup().getAdmin().getId() != userId && split.getGroup().getUsers().contains(user) == false) {
            return ResponseEntity.status(401).build();
        }
        return splitServices.deleteSplit(splitId);
    }

     // Get Splits by User
     @GetMapping("/me")
     public ResponseEntity<List<SplitDTO>> getSplitsByUser() {
         Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
         Long adminId = ((User) principal).getId();
        //  User user = userRepository.findById(adminId).orElseThrow(() -> new RuntimeException("User not found"));
         List<SplitDTO> splits = splitServices.getSplitsByUser(adminId);

         return ResponseEntity.ok(splits);
     }
 
     // Get Splits by Group
     @GetMapping("/group/{groupId}")
     public ResponseEntity<List<SplitDTO>> getSplitsByGroup(@PathVariable Long groupId) {
         Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
         Long userId = ((User) principal).getId();
         Group group = groupRepository.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));
         User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
         // check if user is admin or part of the group
         if (group.getAdmin().getId() != userId && group.getUsers().contains(user) == false) {
             return ResponseEntity.status(401).build();
         }
         List<SplitDTO> splits = splitServices.getSplitsByGroup(groupId);
         return ResponseEntity.ok(splits);
     }
}
