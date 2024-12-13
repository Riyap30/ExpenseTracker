package com.example.demo.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categories")
@Data
@Getter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    public void setName(String name) {
        this.name = name;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    private String name;

    // Flag to indicate if the category is global (applicable to all groups)

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = true)
    private Group group; // Null or a special Group (e.g., "ALL") for global categories

    @OneToMany(mappedBy="category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference // parent side of category-expense relationship
    private List<Expense> expenses;

    @OneToMany(mappedBy="category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference // parent side of category-split relationship
    private List<Split> splits;



    // only getters

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public List<Split> getSplits() {
        return splits;
    }
}
