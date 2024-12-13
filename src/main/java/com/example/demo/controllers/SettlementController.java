package com.example.demo.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTOs.SettlementDTO;
import com.example.demo.entities.*;
import com.example.demo.repositories.SettlementRepository;
import com.example.demo.repositories.SplitRepository;
import com.example.demo.repositories.GroupRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.SettlementServices;
import com.example.demo.services.SplitServices;


@RestController
@RequestMapping("/settlements")
public class SettlementController {
    
    @Autowired
    private SettlementRepository settlementsRepository;

    @Autowired
    private SplitServices splitServices;

    @Autowired
    private SplitRepository splitRepository;

    @Autowired
    private SettlementServices settlementsServices;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    // Create Settlement
    @PostMapping(path = "/{splitId}", consumes = "application/json")
    public ResponseEntity<SettlementDTO> createSettlement(@PathVariable Long splitId,  @RequestBody SettlementDTO settlement) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = ((User) principal).getId();
        // only the payer and the receiver can initiate a settlement. 
        if (settlement.payerId() != userId && settlement.receiverId() != userId) {
            return ResponseEntity.status(401).build();
        }
        Optional<Split> split = splitRepository.findById(splitId);
        
        Optional<Group> group = groupRepository.findById(settlement.groupId());
        if (group.isPresent()) {
            Optional<User> payer = userRepository.findById(settlement.payerId());
            if (payer.isPresent()) {
                Optional<User> receiver = userRepository.findById(settlement.receiverId());
                if (receiver.isPresent()) {
                    Settlements newSettlement = new Settlements();
                    newSettlement.setGroup(group.get());
                    newSettlement.setPayer(payer.get());
                    newSettlement.setReceiver(receiver.get());
                    newSettlement.setAmount(settlement.amount());
                    newSettlement.setSettledDate(settlement.settledDate());
                    Settlements savedSettlement = settlementsServices.savSettlements(newSettlement);
                    SettlementDTO settlementDTO = new SettlementDTO(
                        savedSettlement.getId(),
                        savedSettlement.getAmount(),
                        savedSettlement.getSettledDate(),
                        savedSettlement.getGroup().getId(),
                        savedSettlement.getPayer().getId(),
                        savedSettlement.getPayer().getUsername(),
                        savedSettlement.getReceiver().getId(),
                        savedSettlement.getReceiver().getUsername()
                    );
                    split.get().setSettled(true);
                    splitServices.saveSplit(split.get());
                    return ResponseEntity.ok(settlementDTO);
                } else {
                    return ResponseEntity.notFound().build();
                }
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Get Settlement Details
    @GetMapping("/{settlementId}")
    public ResponseEntity<SettlementDTO> getSettlementById(@PathVariable Long settlementId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = ((User) principal).getId();
        Optional<Settlements> settlement = settlementsRepository.findById(settlementId);
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Group group = groupRepository.findById(settlement.get().getGroup().getId()).orElseThrow(() -> new RuntimeException("Group not found"));
        // if the user is not part of the group or the user is not the admin
        if (group.getAdmin().getId() != userId && group.getUsers().contains(user) == false) {
            return ResponseEntity.status(401).build();
        }

        SettlementDTO settlementDTO = new SettlementDTO(
            settlement.get().getId(),
            settlement.get().getAmount(),
            settlement.get().getSettledDate(),
            settlement.get().getGroup().getId(),
            settlement.get().getPayer().getId(),
            settlement.get().getPayer().getUsername(),
            settlement.get().getReceiver().getId(),
            settlement.get().getReceiver().getUsername()
        );
        return ResponseEntity.ok(settlementDTO);
    }
    
    // Get Settlements by Group
    @GetMapping("/groups/{groupId}")
    public ResponseEntity<List<SettlementDTO>> getSettlementsByGroup(@PathVariable Long groupId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = ((User) principal).getId();
        Optional<Group> group = groupRepository.findById(groupId);
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        // if the user is not part of the group or the user is not the admin
        if (group.get().getAdmin().getId() != userId &&  group.get().getUsers().contains(user) == false) {
            return ResponseEntity.status(401).build();
        }
        List<SettlementDTO> settlements = settlementsServices.findSettlementsByGroupId(groupId);
        return ResponseEntity.ok(settlements);
        
    }

    // Get Settlements by User
    @GetMapping("/user")
    public ResponseEntity<List<SettlementDTO>> getSettlementsByUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = ((User) principal).getId();
        List<SettlementDTO> settlements = settlementsServices.findSettlementsByUsersId(userId);
        return ResponseEntity.ok(settlements);
    }

}

