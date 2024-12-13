package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "splits")
@Data
@Getter
public class Split {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "group_id")
    @JsonBackReference // child-side of the group-split relationship
    private Group group;

    @ManyToOne
    @JoinColumn(name = "payee_id")
    @JsonBackReference // child-side of the user-payee relationship
    private User payee;

    @ManyToOne
    @JoinColumn(name = "payer_id")
    @JsonBackReference // child-side of the user-payer relationship
    private User payer;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonBackReference // child-side of the category-split relationship
    private Category category;

    private Double amount;

    private boolean settled;

    // Constructors, getters, and setters
    public Split() {

    }
    
    public Split(Group group, User payee, User payer, Category category, Double amount) {  
        this.group = group;
        this.payee = payee;
        this.payer = payer;
        this.category = category;
        this.amount = amount;
        this.settled = false;
    }


    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public User getPayee() {
        return payee;
    }

    public void setPayee(User payee) {
        this.payee = payee;
    }

    public User getPayer() {
        return payer;
    }

    public void setPayer(User payer) {
        this.payer = payer;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public boolean getSettled() {
        return settled;
    }

    public void setSettled(boolean settled) {
        this.settled = settled;
    }

    // Other methods

    @Override
    public String toString() {
        return "Split{" +
                "id=" + id +
                ", group=" + group +
                ", payee=" + payee +
                ", payer=" + payer +
                ", category=" + category +
                ", amount=" + amount +
                ", settled=" + settled +
                '}';
    }   



}
