package com.RoomyExpense.tracker.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @Email(message = "Email must be valid")
    private String email;

    @Pattern(
            regexp = "\\d{2,4}-\\d{6,8}",
            message = "Phone number must match the format: AreaCode-Number (e.g., 011-12345678 or 0376-123456)"
    )
    private String phoneNumber;

    private Long houseId; // Can be null if the house is not updated.
}
