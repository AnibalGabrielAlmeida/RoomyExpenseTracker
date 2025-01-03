package com.RoomyExpense.tracker.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ExpenseCreationDTO {
    @NotBlank(message = "The name of the Expense cannot be blank.")
    @Size(max = 100, message = "The name cannot exceed 100 characters.")
    private String name;

    @Size(max = 300, message = "The description cannot exceed 300 characters.")
    private String description;

    @NotNull(message = "Amount is required.")
    @Positive(message = "Amount must be positive.")
    private Double amount;

    @NotBlank(message = "Category is required.")
    @Pattern(regexp = "FIXED|VARIABLE", message = "Category must be FIXED or VARIABLE.")
    private String category;

    @NotNull(message = "Date is required.")
    private LocalDate date;

    @NotNull(message = "House ID is required.")
    private Long houseId;
}
