package com.RoomyExpense.tracker.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@AllArgsConstructor
public class PaymentUpdateDTO {
    @Schema(description = "Amount to update", example = "500.00")
    @Positive(message = "The amount must be positive.")
    private Double amount;

    @Schema(description = "Payment state (PAID or UNPAID)", example = "PAID")
    @Pattern(regexp = "PAID|UNPAID", message = "The state must be PAID or UNPAID.")
    private String state;
}
