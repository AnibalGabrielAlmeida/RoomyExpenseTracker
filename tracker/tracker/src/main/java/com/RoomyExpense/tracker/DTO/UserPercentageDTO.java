package com.RoomyExpense.tracker.DTO;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserPercentageDTO {

    @NotNull(message = "El ID del usuario es obligatorio.")
    private Long userId;

    @NotNull(message = "El porcentaje de división es obligatorio.")
    @DecimalMin(value = "0.0", inclusive = true, message = "El porcentaje no puede ser menor a 0.")
    @DecimalMax(value = "100.0", inclusive = true, message = "El porcentaje no puede ser mayor a 100.")
    private Double divisionPercentage;
}
