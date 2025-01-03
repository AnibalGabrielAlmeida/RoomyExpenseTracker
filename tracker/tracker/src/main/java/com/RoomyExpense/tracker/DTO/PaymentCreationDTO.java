package com.RoomyExpense.tracker.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentCreationDTO {
    @NotNull(message = "The user ID is mandatory.")
    private Long userId;

    @NotNull(message = "The expense split ID is mandatory.")
    private Long expenseSplitId;

    @NotNull(message = "The amount is mandatory.")
    @Positive(message = "The amount must be positive.")
    private Double amount;
}
