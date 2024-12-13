package com.example.demo.controllers;

import java.util.ArrayList;
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

import com.example.demo.DTOs.GroupDTO;
import com.example.demo.DTOs.SettlementDTO;
import com.example.demo.DTOs.SplitDTO;
import com.example.demo.DTOs.UserDTO;
import com.example.demo.entities.*;
import com.example.demo.repositories.GroupRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.GroupServices;


@RestController
@RequestMapping("/groups")
public class GroupController {
    
    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupServices groupServices;

    @Autowired
    private UserRepository userRepository;


    
    // Create Group
    @PostMapping(path = "/", consumes = "application/json")
    public ResponseEntity<GroupDTO> createGroup(@RequestBody GroupDTO group) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = ((User) principal).getId();
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        List<User> userList = new ArrayList<>();
        userList.add(user);
        Group newGroup = new Group();
        newGroup.setName(group.name());
        newGroup.setDate(group.date());
        newGroup.setAdmin(user);
        newGroup.setUsers(userList);
        groupServices.saveGroup(newGroup);
        GroupDTO newGroupDTO = new GroupDTO(newGroup.getId(), newGroup.getName(), newGroup.getDate(), newGroup.getAdmin().getId(), newGroup.getAdmin().getUsername());
        return ResponseEntity.ok(newGroupDTO);
    
    }

    // Get Group Details
    @GetMapping(path = "/{groupId}", produces = "application/json")
    public ResponseEntity<GroupDTO> getGroupById(@PathVariable Long groupId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = ((User) principal).getId();
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Optional<Group> group = groupRepository.findById(groupId);
        Optional<GroupDTO> groupDTO = groupServices.findGroupById(groupId);
        // check if user is a member of the group
        if (group.get().getAdmin().getId() != userId && group.get().getUsers().contains(user) == false) {
            return ResponseEntity.status(401).build();
        }
        return groupDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }



    // Update Group Details
    @PutMapping("/{groupId}")
    public ResponseEntity<GroupDTO> updateGroup(@PathVariable Long groupId, @RequestBody GroupDTO groupDetails) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = ((User) principal).getId();
        
        // check if the user is the admin of the group
        if (groupDetails.admin() != userId) {
            return ResponseEntity.status(401).build();
        }
        Group updatedGroup = groupServices.updateGroup(groupId, groupDetails);
        GroupDTO groupDTO = new GroupDTO(updatedGroup.getId(), updatedGroup.getName(), updatedGroup.getDate(), updatedGroup.getAdmin().getId(), updatedGroup.getAdmin().getUsername());
        return ResponseEntity.ok(groupDTO);
    }

    // Delete Group
    @DeleteMapping("/{groupId}")
    public ResponseEntity<Object> deleteGroup(@PathVariable Long groupId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = ((User) principal).getId();
        // check if the user is the admin of the group
        if (groupRepository.findById(groupId).get().getAdmin().getId() != userId) {
            return ResponseEntity.status(401).build();
        }
        return groupServices.deleteGroup(groupId);
    }

    // get user groups
    @GetMapping("/me")
    public ResponseEntity<List<GroupDTO>> getUserGroups() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = ((User) principal).getId();
        List<GroupDTO> groups = groupServices.getUserGroups(userId);
        return ResponseEntity.ok(groups);
    }


    // Add User to Group
    @PostMapping("/{groupId}/users/{userId}")
    public ResponseEntity<GroupDTO> addUserToGroup(@PathVariable long groupId, @PathVariable long userId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long adminId = ((User) principal).getId();
        // check if user is already in the group
        if (groupRepository.findById(groupId).get().getUsers().contains(userRepository.findById(userId).get())) {
            return ResponseEntity.status(400).build();
        }
        // check if user is the admin of the group
        if (groupRepository.findById(groupId).get().getAdmin().getId() != adminId) {
            return ResponseEntity.status(401).build();
        }
        Group newUser = groupServices.addUserToGroup(groupId, userId);
        GroupDTO newGroupDTO = new GroupDTO(newUser.getId(), newUser.getName(), newUser.getDate(), newUser.getAdmin().getId(), newUser.getAdmin().getUsername());
        return ResponseEntity.ok(newGroupDTO);

    }

    // Remove User from Group
    @DeleteMapping("/{groupId}/users/{userId}")
    public ResponseEntity<GroupDTO> removeUserFromGroup(@PathVariable long groupId, @PathVariable long userId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long adminId = ((User) principal).getId();
        // check if user is the admin of the group
        if (groupRepository.findById(groupId).get().getAdmin().getId() != adminId) {
            return ResponseEntity.status(401).build();
        }
        // checl if the user is the admin of the group
        if (adminId == userId) {
            return ResponseEntity.status(400).build();
        }
        Group updatedGroup = groupServices.removeUserFromGroup(groupId, userId);
        GroupDTO groupDTO = new GroupDTO(updatedGroup.getId(), updatedGroup.getName(), updatedGroup.getDate(), updatedGroup.getAdmin().getId(), updatedGroup.getAdmin().getUsername());
        return ResponseEntity.ok(groupDTO);
    }

    // Get Group Users
    @GetMapping("/{groupId}/users")
    public ResponseEntity<List<UserDTO>> getGroupUsers(@PathVariable Long groupId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = ((User) principal).getId();
        Optional<Group> group = groupRepository.findById(groupId);
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        // check if user is admin or part of the group
        if (group.get().getAdmin().getId() != userId && group.get().getUsers().contains(user) == false) {
            return ResponseEntity.status(401).build();
        }
        List<UserDTO> users = groupServices.getGroupUsers(groupId);
        return ResponseEntity.ok(users);
    }

    // Get Group Splits
    @GetMapping("/{groupId}/splits")
    public ResponseEntity<List<SplitDTO>> getGroupSplits(@PathVariable Long groupId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = ((User) principal).getId();
        Optional<Group> group = groupRepository.findById(groupId);
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        // check if user is admin or part of the group
        if (group.get().getAdmin().getId() != userId && group.get().getUsers().contains(user) == false) {
            return ResponseEntity.status(401).build();
        }
        List<SplitDTO> splits = groupServices.getGroupSplits(groupId);
        return ResponseEntity.ok(splits);
    }

    // Get Group Settlements
    @GetMapping("/{groupId}/settlements")
    public ResponseEntity<List<SettlementDTO>> getGroupSettlements(@PathVariable Long groupId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = ((User) principal).getId();
        Optional<Group> group = groupRepository.findById(groupId);
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        // check if user is admin or part of the group
        if (group.get().getAdmin().getId() != userId && group.get().getUsers().contains(user) == false) {
            return ResponseEntity.status(401).build();
        }
        List<SettlementDTO> settlements = groupServices.getGroupSettlements(groupId);
        return ResponseEntity.ok(settlements);
    }

}

