package com.RoomyExpense.tracker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "expenses")
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Double amount;

    public enum Category {
        FIXED, VARIABLE
    }

    @Enumerated(EnumType.STRING)
    private Category category;

    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "house_id")
    @JsonIgnore
    private House house;

    @OneToMany(mappedBy = "expense")
    private List<ExpenseSplit> expenseSplit;
}
