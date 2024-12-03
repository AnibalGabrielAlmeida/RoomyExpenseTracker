package com.RoomyExpense.tracker.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentCreationDTO {
    @NotNull(message = "El ID del usuario es obligatorio.")
    private Long userId;

    @NotNull(message = "El ID del gasto es obligatorio.")
    private Long expenseId;

    @NotNull(message = "El monto es obligatorio.")
    @Positive(message = "El monto debe ser positivo.")
    private Double amount;
}
