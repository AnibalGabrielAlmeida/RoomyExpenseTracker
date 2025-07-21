package com.RoomyExpense.tracker.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ExpenseUpdateDTO {
    @Schema(description = "Expense name", example = "Alquiler")
    @Size(max = 100, message = "The name cannot exceed 100 characters.")
    private String name;

    @Schema(description = "Expense amount", example = "1200.50")
    @Positive(message = "The amount must be positive.")
    private Double amount;

    @Schema(description = "Expense category (FIXED o VARIABLE)", example = "FIXED")
    @Pattern(regexp = "FIXED|VARIABLE", message = "The category must be FIXED or VARIABLE.")
    private String category;

    @Schema(description = "Expense description", example = "Pago mensual del alquiler")
    @Size(max = 300, message = "The description cannot exceed 300 characters.")
    private String description;

    @Schema(description = "Date of the expense", example = "2024-07-21")
    private LocalDate date;
}
