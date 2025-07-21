package com.RoomyExpense.tracker.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    @Schema(description = "User ID", example = "7")
    private Long id;

    @Schema(description = "User name", example = "Gabriel Pérez")
    private String name;

    @Schema(description = "User email", example = "gabriel@mail.com")
    private String email;

    @Schema(description = "Phone number (AreaCode-Number)", example = "011-12345678")
    private String phoneNumber;

    @Schema(description = "Registration date", example = "2024-07-21")
    private LocalDate registrationDate;

    @Schema(description = "Name of the user's house", example = "Casa Central")
    private String houseName; // Nombre de la casa del usuario

    @Schema(description = "URL for more information about the house", example = "/api/house/getById/2")
    private String houseUrl; // URL para obtener más información sobre la casa

    @Schema(description = "User role in the house (ADMIN or ROOMY)", example = "ADMIN")
    private String role; // Rol del usuario en la casa (e.g., "ADMIN" o "ROOMY")
}
