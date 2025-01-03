package com.RoomyExpense.tracker.DTO;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserPercentageDTO {

    @NotNull(message = "The user ID is required.")
    private Long userId;

    @NotNull(message = "The division percentage is required.")
    @DecimalMin(value = "0.0", inclusive = true, message = "The percentage cannot be less than 0.")
    @DecimalMax(value = "100.0", inclusive = true, message = "The percentage cannot exceed 100.")
    private Double divisionPercentage;
}
