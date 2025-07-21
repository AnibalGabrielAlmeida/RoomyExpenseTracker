package com.RoomyExpense.tracker.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class PaymentDTO {
    @Schema(description = "Date of the payment", example = "2024-07-21")
    private LocalDate paymentDate;

    @Schema(description = "Amount paid", example = "500.00")
    private Double amount;

    @Schema(description = "Name of the user who made the payment", example = "Gabriel Pérez")
    private String userName;

    @Schema(description = "Name of the expense associated with the payment", example = "Alquiler")
    private String expenseName;
}
