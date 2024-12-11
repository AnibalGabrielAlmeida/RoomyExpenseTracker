package com.RoomyExpense.tracker.DTO;

import com.RoomyExpense.tracker.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
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
public class UserRoleUpdateDTO {

    @NotNull(message = "Role must not be null")
    private User.UserRole role;

}


