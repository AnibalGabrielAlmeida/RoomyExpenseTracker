package com.RoomyExpense.tracker.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class HouseDTO {
    private Long id; // Incluido para referencias internas, pero podría ocultarse si no es necesario para el cliente.
    private String name;
    private String address;
    private List<String> roommates; // Solo nombres de los compañeros de casa.
}

