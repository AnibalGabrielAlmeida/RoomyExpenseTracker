package com.RoomyExpense.tracker.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@AllArgsConstructor
public class ExpenseCreationDTO {

    @Schema(description = "Nombre del gasto", example = "Alquiler de abril")
    @NotBlank(message = "The name of the Expense cannot be blank.")
    @Size(max = 100, message = "The name cannot exceed 100 characters.")
    private String name;

    @Schema(description = "Descripción del gasto", example = "Pago mensual de alquiler")
    @Size(max = 300, message = "The description cannot exceed 300 characters.")
    private String description;

    @Schema(description = "Monto del gasto", example = "1500.00")
    @NotNull(message = "Amount is required.")
    @Positive(message = "Amount must be positive.")
    private Double amount;

    @Schema(description = "Categoría del gasto", example = "FIXED")
    @NotBlank(message = "Category is required.")
    @Pattern(regexp = "FIXED|VARIABLE", message = "Category must be FIXED or VARIABLE.")
    private String category;

    @Schema(description = "Fecha del gasto", example = "2023-04-01")
    @NotNull(message = "Date is required.")
    private LocalDate date;

    @Schema(description = "ID de la casa a la que pertenece el gasto", example = "1")
    @NotNull(message = "House ID is required.")
    private Long houseId;
}
