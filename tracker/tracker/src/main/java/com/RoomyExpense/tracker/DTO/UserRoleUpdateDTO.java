package com.RoomyExpense.tracker.DTO;

import com.RoomyExpense.tracker.model.User;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleUpdateDTO {

    @Schema(description = "Role to assign (ADMIN or ROOMY)", example = "ADMIN")
    @NotNull(message = "Role must not be null")
    private User.UserRole role;

}


