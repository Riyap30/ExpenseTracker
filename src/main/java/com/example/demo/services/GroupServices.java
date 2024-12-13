package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.repositories.GroupRepository;
import com.example.demo.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

import com.example.demo.DTOs.GroupDTO;
import com.example.demo.DTOs.SettlementDTO;
import com.example.demo.DTOs.SplitDTO;
import com.example.demo.DTOs.UserDTO;
import com.example.demo.entities.*;


@Service
public class GroupServices {
    
    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;


    // findAllGroups
    public List<GroupDTO> findAllGroups() {
        List<GroupDTO> groups = groupRepository.findAll().stream().map(
            group -> new GroupDTO(
                group.getId(),
                group.getName(),
                group.getDate(),
                group.getAdmin().getId(),
                group.getAdmin().getUsername()
            )
        ).toList();
        return groups;
    }

    // find groups by userID
    public List<GroupDTO> findGroupsByUserId(Long id) {
        List<GroupDTO> groups = groupRepository.findByUsersId(id).stream().map(
            group -> new GroupDTO(
                group.getId(),
                group.getName(),
                group.getDate(),
                group.getAdmin().getId(),
                group.getAdmin().getUsername()
            )
        ).toList();
        return groups;
    }

    // findGroupById
    public Optional<GroupDTO> findGroupById(Long id) {
        Optional<GroupDTO> group = groupRepository.findById(id).map(
            group1 -> new GroupDTO(
                group1.getId(),
                group1.getName(),
                group1.getDate(),
                group1.getAdmin().getId(),
                group1.getAdmin().getUsername()
        ));
        return group;
    }

    // update Group Details
    public Group updateGroup(long id, GroupDTO groupDTO) {
        Group group = groupRepository.findById(id).orElseThrow(() -> new RuntimeException("Group not found"));
        group.setName(groupDTO.name());
        group.setDate(groupDTO.date());
        group.setAdmin(userRepository.findById(groupDTO.admin()).orElseThrow(() -> new RuntimeException("User not found")));
        return groupRepository.save(group);
    }

    // saveGroup
    public Group saveGroup(Group group) {
        return groupRepository.save(group);
    }


    // deleteGroupById
    public ResponseEntity<Object> deleteGroup(Long id) {
        groupRepository.deleteById(id);
        return null;
    }

    public List<GroupDTO> getUserGroups(Long id) {
        List<GroupDTO> groups = groupRepository.findByUsersId(id).stream().map(
            group -> new GroupDTO(
                group.getId(),
                group.getName(),
                group.getDate(),
                group.getAdmin().getId(),
                group.getAdmin().getUsername()
            )
        ).toList();
        return groups;
        
    }

    // addUserToGroup
    public Group addUserToGroup(Long groupId, Long userId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        if (!group.getUsers().contains(user)) {
            group.getUsers().add(user);
        } else {
            throw new RuntimeException("User already in group");
        }
        return groupRepository.save(group);
    }


    // removeUserFromGroup
    public Group removeUserFromGroup(Long groupId, Long userId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        group.getUsers().remove(user);
        return groupRepository.save(group);
    }

    // get group users
    public List<UserDTO> getGroupUsers(Long groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));
        List<UserDTO> groups = group.getUsers().stream().map(
            user -> new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail()
            )
        ).toList();
        return groups;
    }
    
    // get group splits
    public List<SplitDTO> getGroupSplits(Long groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));
        List<SplitDTO> splits = group.getSplits().stream().filter(split -> !split.getSettled()).map(
            
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

    // get Group Settlements
    public List<SettlementDTO> getGroupSettlements(Long groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));
        List<SettlementDTO> settlements = group.getSettlements().stream().map(
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
}
