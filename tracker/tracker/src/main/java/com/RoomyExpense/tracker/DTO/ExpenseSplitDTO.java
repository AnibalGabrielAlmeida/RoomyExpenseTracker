package com.RoomyExpense.tracker.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@AllArgsConstructor
public class ExpenseSplitDTO {
    @Schema(description = "Expense split ID", example = "10")
    private Long id;

    @Schema(description = "Name of the user assigned to this split", example = "Juan Pérez")
    private String userName;

    @Schema(description = "Name of the expense", example = "Alquiler")
    private String expenseName;

    @Schema(description = "Amount assigned to the user for this split", example = "400.0")
    private Double sharedAmount;
}

