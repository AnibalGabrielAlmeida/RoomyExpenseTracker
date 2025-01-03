package com.RoomyExpense.tracker.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ExpenseUpdateDTO {
    @Size(max = 100, message = "The name cannot exceed 100 characters.")
    private String name;

    @Positive(message = "The amount must be positive.")
    private Double amount;

    @Pattern(regexp = "FIXED|VARIABLE", message = "The category must be FIXED or VARIABLE.")
    private String category;

    @Size(max = 300, message = "The description cannot exceed 300 characters.")
    private String description;

    private LocalDate date;
}
