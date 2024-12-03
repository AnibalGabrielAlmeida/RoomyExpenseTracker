package com.RoomyExpense.tracker.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExpenseShareDTO {
    private Long id;
    private String userName; // Nombre del usuario que comparte el gasto.
    private Double totalAmount; // Monto total del gasto.
    private Double divisionPercentage; // Porcentaje de división del gasto.
    private Double sharedAmount; // Monto compartido calculado.
}

