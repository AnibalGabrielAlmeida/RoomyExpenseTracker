package com.RoomyExpense.tracker.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HouseCreationDTO {

    @Schema(description = "Nombre de la casa", example = "Casa de Verano")
    @NotBlank(message = "The name of the house cannot be blank.")
    @Size(max = 100, message = "The name cannot exceed 100 characters.")
    private String name;

    @Schema(description = "Descripción de la casa", example = "Una hermosa casa de verano con vista al mar.")
    @NotBlank(message = "The address cannot be blank.")
    @Size(max = 200, message = "The address cannot exceed 200 characters.")
    private String address;
}
