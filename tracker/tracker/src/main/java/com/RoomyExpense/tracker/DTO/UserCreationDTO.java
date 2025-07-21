package com.RoomyExpense.tracker.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
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
    
    @Schema(description = "Nombre completo del usuario", example = "Gabriel Pérez")
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @Schema(description = "Email del usuario", example = "gabi@example.com")
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email must be valid")
    private String email;

    @Schema(description = "Contraseña del usuario", example = "MiClaveSegura123")
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @Schema(description = "Número de teléfono del usuario", example = "011-12345678")
    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(
            regexp = "\\d{2,4}-\\d{6,8}",
            message = "Phone number must match the format: AreaCode-Number (e.g., 011-12345678 or 0376-123456)"
    )
    private String phoneNumber;

    @Schema(description = "ID de la casa a la que pertenece el usuario", example = "1")
    private Long houseId;

    @Schema(description = "Rol del usuario", example = "ADMIN")
    @NotBlank(message = "Role cannot be blank")
    @Pattern(regexp = "ADMIN|ROOMY", message = "Role must be either ADMIN or ROOMY")
    private String role;
}
