package com.RoomyExpense.tracker.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class PaymentDTO {
    private LocalDate paymentDate;
    private Double amount;
    private String userName;
    private String expenseName;
    private String state;
}
