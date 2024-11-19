package com.RoomyExpense.tracker.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HouseCreationDTO {
    @NotBlank(message = "El nombre de la casa no puede estar vacío.")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres.")
    private String name;

    @NotBlank(message = "La dirección no puede estar vacía.")
    @Size(max = 200, message = "La dirección no puede superar los 200 caracteres.")
    private String address;
}

