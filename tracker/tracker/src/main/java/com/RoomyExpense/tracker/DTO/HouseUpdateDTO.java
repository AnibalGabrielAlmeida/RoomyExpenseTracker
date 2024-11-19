package com.RoomyExpense.tracker.DTO;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HouseUpdateDTO {
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres.")
    private String name;

    @Size(max = 200, message = "La dirección no puede superar los 200 caracteres.")
    private String address;
}

