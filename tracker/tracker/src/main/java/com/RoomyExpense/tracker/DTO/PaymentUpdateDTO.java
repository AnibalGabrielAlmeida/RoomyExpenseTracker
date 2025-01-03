package com.RoomyExpense.tracker.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentUpdateDTO {
    @Positive(message = "The amount must be positive.")
    private Double amount;

    @Pattern(regexp = "PAID|UNPAID", message = "The state must be PAID or UNPAID.")
    private String state;
}
