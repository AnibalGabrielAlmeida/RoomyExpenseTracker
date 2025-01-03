package com.RoomyExpense.tracker.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreationDTO {
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(
            regexp = "\\d{2,4}-\\d{6,8}",
            message = "Phone number must match the format: AreaCode-Number (e.g., 011-12345678 or 0376-123456)"
    )
    private String phoneNumber;

    private Long houseId;

    @NotBlank(message = "Role cannot be blank")
    @Pattern(regexp = "ADMIN|ROOMY", message = "Role must be either ADMIN or ROOMY")
    private String role;
}
