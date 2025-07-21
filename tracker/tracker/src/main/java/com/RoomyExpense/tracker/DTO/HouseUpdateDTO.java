package com.RoomyExpense.tracker.DTO;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@AllArgsConstructor
public class HouseUpdateDTO {
    @Schema(description = "House name", example = "Casa Central")
    @Size(max = 100, message = "The name cannot exceed 100 characters.")
    private String name;

    @Schema(description = "House address", example = "Calle Falsa 123")
    @Size(max = 200, message = "The address cannot exceed 200 characters.")
    private String address;
}
