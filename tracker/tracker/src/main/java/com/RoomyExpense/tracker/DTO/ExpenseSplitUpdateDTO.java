package com.RoomyExpense.tracker.DTO;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseSplitUpdateDTO {
    @Positive(message = "El monto debe ser positivo.")
    private Double amount;


}
