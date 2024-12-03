package com.RoomyExpense.tracker.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExpenseShareCreationDTO {
    @NotNull(message = "El ID del gasto es obligatorio.")
    private Long expenseId;

    @NotNull(message = "El ID del usuario es obligatorio.")
    private Long userId;

    @NotNull(message = "El monto total es obligatorio.")
    @Positive(message = "El monto total debe ser positivo.")
    private Double totalAmount;

    @NotNull(message = "El porcentaje de división es obligatorio.")
    @DecimalMin(value = "0.0", inclusive = true, message = "El porcentaje no puede ser menor a 0.")
    @DecimalMax(value = "100.0", inclusive = true, message = "El porcentaje no puede ser mayor a 100.")
    private Double divisionPercentage;
}

