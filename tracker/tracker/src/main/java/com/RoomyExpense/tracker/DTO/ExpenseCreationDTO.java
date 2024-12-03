package com.RoomyExpense.tracker.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ExpenseCreationDTO {
    @NotBlank(message = "El nombre del gasto no puede estar vacío.")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres.")
    private String name;

    @Size(max = 300, message = "La descripción no puede superar los 300 caracteres.")
    private String description;

    @NotNull(message = "El monto es obligatorio.")
    @Positive(message = "El monto debe ser positivo.")
    private Double amount;

    @NotBlank(message = "La categoría es obligatoria.")
    @Pattern(regexp = "FIXED|VARIABLE", message = "La categoría debe ser FIXED o VARIABLE.")
    private String category;

    @NotNull(message = "La fecha es obligatoria.")
    private LocalDate date;

    @NotNull(message = "El ID de la casa es obligatorio.")
    private Long houseId;
}

