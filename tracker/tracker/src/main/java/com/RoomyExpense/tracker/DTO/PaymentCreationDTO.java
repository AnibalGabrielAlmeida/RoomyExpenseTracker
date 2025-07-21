package com.RoomyExpense.tracker.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@AllArgsConstructor
public class PaymentCreationDTO {

    @Schema(description = "ID del usuario que realiza el pago", example = "1")
    @NotNull(message = "The user ID is mandatory.")
    private Long userId;

    @Schema(description = "ID del gasto que se está pagando", example = "1")
    @NotNull(message = "The expense split ID is mandatory.")
    private Long expenseSplitId;

    @Schema(description = "Monto del pago", example = "500.00")
    @NotNull(message = "The amount is mandatory.")
    @Positive(message = "The amount must be positive.")
    private Double amount;
}
