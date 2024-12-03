package com.RoomyExpense.tracker.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentUpdateDTO {
    @Positive(message = "El monto debe ser positivo.")
    private Double amount;

    @Pattern(regexp = "PAID|UNPAID", message = "El estado debe ser PAID o UNPAID.")
    private String state;
}

