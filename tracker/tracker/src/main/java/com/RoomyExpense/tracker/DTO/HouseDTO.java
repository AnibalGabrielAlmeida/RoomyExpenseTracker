package com.RoomyExpense.tracker.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Data
@AllArgsConstructor
public class HouseDTO {
    @Schema(description = "House ID", example = "5")
    private Long id; // Incluido para referencias internas, pero podría ocultarse si no es necesario para el cliente.

    @Schema(description = "House name", example = "Casa Central")
    private String name;

    @Schema(description = "House address", example = "Calle Falsa 123")
    private String address;

    @Schema(description = "List of roommates' names", example = "[\"Juan Pérez\", \"Ana Gómez\"]")
    private List<String> roommates; // Solo nombres de los compañeros de casa.
}

