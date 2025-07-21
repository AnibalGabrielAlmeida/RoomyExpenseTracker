package com.RoomyExpense.tracker.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {

    @Schema(description = "User name", example = "Gabriel Pérez")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @Schema(description = "User email", example = "gabriel@mail.com")
    @Email(message = "Email must be valid")
    private String email;

    @Schema(description = "Phone number (AreaCode-Number)", example = "011-12345678")
    @Pattern(
            regexp = "\\d{2,4}-\\d{6,8}",
            message = "Phone number must match the format: AreaCode-Number (e.g., 011-12345678 or 0376-123456)"
    )
    private String phoneNumber;

    @Schema(description = "ID of the house to assign", example = "2")
    private Long houseId; // Can be null if the house is not updated.
}
