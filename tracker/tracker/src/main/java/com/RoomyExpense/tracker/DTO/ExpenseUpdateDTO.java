package com.RoomyExpense.tracker.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ExpenseUpdateDTO {
    //translate
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres.")
    private String name;

    @Positive(message = "El monto debe ser positivo.")
    private Double amount;

    @Pattern(regexp = "FIXED|VARIABLE", message = "La categoría debe ser FIXED o VARIABLE.")
    private String category;

    @Size(max = 300, message = "La descripción no puede superar los 300 caracteres.")
    private String description;

    private LocalDate date;
}

