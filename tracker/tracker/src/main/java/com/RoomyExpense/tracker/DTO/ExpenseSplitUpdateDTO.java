package com.RoomyExpense.tracker.DTO;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseSplitUpdateDTO {

    @Schema(description = "Monto actualizado para la división del gasto", example = "75.0")
    @Positive(message = "The amount must be positive.")
    private Double amount;
}
