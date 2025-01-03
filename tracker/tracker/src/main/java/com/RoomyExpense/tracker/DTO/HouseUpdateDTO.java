package com.RoomyExpense.tracker.DTO;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HouseUpdateDTO {
    @Size(max = 100, message = "The name cannot exceed 100 characters.")
    private String name;

    @Size(max = 200, message = "The address cannot exceed 200 characters.")
    private String address;
}
