package com.RoomyExpense.tracker.DTO;



import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ExpenseDTO {
    private Long id;
    private String name;
    private String description;
    private Double amount;
    private String category; // FIXED o VARIABLE
    private LocalDate date;
    private String houseName; // Nombre de la casa asociada al gasto.
    private Long houseId;
}

