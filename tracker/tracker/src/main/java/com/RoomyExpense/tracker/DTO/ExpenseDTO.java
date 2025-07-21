package com.RoomyExpense.tracker.DTO;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@AllArgsConstructor
public class ExpenseDTO {
    @Schema(description = "Expense ID", example = "1")
    private Long id;

    @Schema(description = "Expense name", example = "Alquiler")
    private String name;

    @Schema(description = "Expense description", example = "Pago mensual del alquiler")
    private String description;

    @Schema(description = "Expense amount", example = "1200.50")
    private Double amount;

    @Schema(description = "Expense category (FIXED o VARIABLE)", example = "FIXED")
    private String category;

    @Schema(description = "Date of the expense", example = "2024-07-21")
    private LocalDate date;

    @Schema(description = "Name of the associated house", example = "Casa Central")
    private String houseName;

    @Schema(description = "ID of the associated house", example = "2")
    private Long houseId;
}

