package com.RoomyExpense.tracker.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private LocalDate registrationDate;
    private String houseName; // Nombre de la casa del usuario
    private String houseUrl; // URL para obtener más información sobre la casa
    private String role; // Rol del usuario en la casa (e.g., "ADMIN" o "ROOMY")
}
