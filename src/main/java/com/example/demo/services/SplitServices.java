package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.DTOs.SplitDTO;
import com.example.demo.entities.*;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.repositories.GroupRepository;
import com.example.demo.repositories.SplitRepository;
import com.example.demo.repositories.UserRepository;


@Service
public class SplitServices {
    
    @Autowired
    private SplitRepository splitRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    
    // find all splits
    public List<SplitDTO> findAllSplits() {
        List<SplitDTO> splits = splitRepository.findAll().stream().map(
            split -> new SplitDTO(
                split.getId(),
                split.getAmount(),
                split.getGroup().getId(),
                split.getPayer().getId(),
                split.getPayer().getUsername(),
                split.getPayee().getId(),
                split.getPayee().getUsername(),
                split.getCategory().getName(),
                split.getSettled(),
                ""
            )
        ).toList();
        return splits;
    }

    // find split by id
    public Optional<SplitDTO> findSplitById(Long id) {
        Optional<SplitDTO> splitFound = splitRepository.findById(id).map(
            split -> new SplitDTO(
                split.getId(),
                split.getAmount(),
                split.getGroup().getId(),
                split.getPayer().getId(),
                split.getPayer().getUsername(),
                split.getPayee().getId(),
                split.getPayee().getUsername(),
                split.getCategory().getName(),
                split.getSettled(),
                ""
            )
        );
        return splitFound;
    }


    // get split by user id
    public List<SplitDTO> getSplitsByUser(Long userId) {
        List<SplitDTO> splits = splitRepository.findByPayerIdOrPayeeId(userId, userId).stream().map(
            split -> new SplitDTO(
                split.getId(),
                split.getAmount(),
                split.getGroup().getId(),
                split.getPayer().getId(),
                split.getPayer().getUsername(),
                split.getPayee().getId(),
                split.getPayee().getUsername(),
                split.getCategory().getName(),
                split.getSettled(),
                ""
            )
        ).toList();
        return splits;
    }

    // get splits by group id
    public List<SplitDTO> getSplitsByGroup(Long groupId) {
        List<SplitDTO> splits = splitRepository.findByGroupId(groupId).stream().map(
            split -> new SplitDTO(
                split.getId(),
                split.getAmount(),
                split.getGroup().getId(),
                split.getPayer().getId(),
                split.getPayer().getUsername(),
                split.getPayee().getId(),
                split.getPayee().getUsername(),
                split.getCategory().getName(),
                split.getSettled(),
                ""
            )
        ).toList();
        return splits;
    }

    // update split
    public Split updateSplit(long id, SplitDTO splitDetails) {
        Split split = splitRepository.findById(id).orElseThrow(() -> new RuntimeException("Split not found"));
        split.setAmount(splitDetails.amount());
        split.setGroup(groupRepository.findById(splitDetails.groupId()).orElseThrow(() -> new RuntimeException("Group not found")));
        split.setPayer(userRepository.findById(splitDetails.payerId()).orElseThrow(() -> new RuntimeException("Payer not found")));
        split.setPayee(userRepository.findById(splitDetails.payeeId()).orElseThrow(() -> new RuntimeException("Payee not found")));
        split.setCategory(categoryRepository.findByName(splitDetails.categoryName()).orElseThrow(() -> new RuntimeException("Category not found")));
        split.setSettled(splitDetails.settled());
        return splitRepository.save(split);
        
    }

    // save split
    public Split saveSplit(Split split){
        return splitRepository.save(split);
    }

    
    // delete split by id
    public Object deleteSplit(long id) {
        splitRepository.deleteById(id);
        return null;
    }

    // find split by payer id and payee id
    public Optional<Split> findSettlementByPayerIdAndPayeeId(long payerId, long payeeId){
        return splitRepository.findByPayerIdAndPayeeId(payerId, payeeId);
    }
    
}
