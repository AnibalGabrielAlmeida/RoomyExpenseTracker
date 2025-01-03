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
    @NotNull(message = "Expense ID is required.")
    private Long expenseId;

    @NotEmpty(message = "The list of users cannot be empty.")
    private List<UserPercentageDTO> userPercentages;
}
