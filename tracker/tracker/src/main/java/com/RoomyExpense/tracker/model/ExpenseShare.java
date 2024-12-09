package com.RoomyExpense.tracker.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "expenses_share")
public class ExpenseShare {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "expense_id")
        @JsonBackReference
        private Expense expense;

        @ManyToOne
        @JoinColumn(name = "user_id")
        @JsonBackReference
        private User user;

        private Double totalAmount;

        private Double divisionPercentage; // Porcentaje de división del gasto

        private Double sharedAmount; // Monto calculado según el porcentaje
    }


