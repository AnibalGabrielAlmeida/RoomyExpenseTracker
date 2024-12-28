package com.RoomyExpense.tracker.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
@AllArgsConstructor
public class ExpenseSplitCreationDTO {
    @NotNull(message = "El ID del gasto es obligatorio.")
    private Long expenseId;

    @NotEmpty(message = "La lista de usuarios no puede estar vacía.")
    private List<UserPercentageDTO> userPercentages;

}

