package com.RoomyExpense.tracker.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class PaymentDTO {
    private Long id;
    private LocalDate paymentDate;
    private Double amount;
    private String state; // PAID o UNPAID
    private String userName; // Nombre del usuario que realizó el pago.
    private String expenseName; // Nombre del gasto al que está asociado.
}