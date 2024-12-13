package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.repositories.SettlementRepository;

import java.util.List;
import java.util.Optional;

import com.example.demo.DTOs.SettlementDTO;
import com.example.demo.entities.*;

@Service
public class SettlementServices {
    
    @Autowired
    private SettlementRepository settlementRepository;

    // find all settlements
    public List<SettlementDTO> findAllSettlements() {
        List<SettlementDTO> settlements = settlementRepository.findAll().stream().map(
            settlement -> new SettlementDTO(
                settlement.getId(),
                settlement.getAmount(),
                settlement.getSettledDate(),
                settlement.getGroup().getId(),
                settlement.getPayer().getId(),
                settlement.getPayer().getUsername(),
                settlement.getReceiver().getId(),
                settlement.getReceiver().getUsername()
            )
        ).toList();
        return settlements;
    }

    // find settlement by id
    public Optional<SettlementDTO> findSettlementsById(Long id) {
        Optional<SettlementDTO> settlements = settlementRepository.findById(id).map(
            settlement -> new SettlementDTO(
                settlement.getId(),
                settlement.getAmount(),
                settlement.getSettledDate(),
                settlement.getGroup().getId(),
                settlement.getPayer().getId(),
                settlement.getPayer().getUsername(),
                settlement.getReceiver().getId(),
                settlement.getReceiver().getUsername()
            )
        );
        return settlements;
    }

    // find settlements by group id
    public List<SettlementDTO> findSettlementsByGroupId(Long groupId) {
        List<SettlementDTO> settlements = settlementRepository.findByGroupId(groupId).stream().map(
            settlement -> new SettlementDTO(
                settlement.getId(),
                settlement.getAmount(),
                settlement.getSettledDate(),
                settlement.getGroup().getId(),
                settlement.getPayer().getId(),
                settlement.getPayer().getUsername(),
                settlement.getReceiver().getId(),
                settlement.getReceiver().getUsername()
            )
        ).toList();
        return settlements;
    }

    // find settlements by user id
    public List<SettlementDTO> findSettlementsByUsersId(Long userId) {
        List<SettlementDTO> settlements = settlementRepository.findByPayerIdOrReceiverId(userId, userId).stream().map(
            settlement -> new SettlementDTO(
                settlement.getId(),
                settlement.getAmount(),
                settlement.getSettledDate(),
                settlement.getGroup().getId(),
                settlement.getPayer().getId(),
                settlement.getPayer().getUsername(),
                settlement.getReceiver().getId(),
                settlement.getReceiver().getUsername()
            )
        ).toList();
        return settlements;
    }

    // save settlement
    public Settlements savSettlements(Settlements settlements) {
        Settlements settlement = settlementRepository.save(settlements);
        return settlement;
    }

    // delete settlement by id
    public void deleteSettlements(long id) {
        settlementRepository.deleteById(id);
    }
}
