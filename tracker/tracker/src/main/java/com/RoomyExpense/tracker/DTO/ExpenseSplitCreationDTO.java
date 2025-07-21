package com.RoomyExpense.tracker.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@Data
@AllArgsConstructor
public class ExpenseSplitCreationDTO {

    @Schema(description = "ID del gasto al que se le realizará la división", example = "1")
    @NotNull(message = "Expense ID is required.")
    private Long expenseId;

    @Schema(description = "Lista de usuarios y sus porcentajes para dividir el gasto", example = "[{\"userId\": 1, \"percentage\": 50}, {\"userId\": 2, \"percentage\": 50}]")
    @NotEmpty(message = "The list of users cannot be empty.")
    private List<UserPercentageDTO> userPercentages;
}
