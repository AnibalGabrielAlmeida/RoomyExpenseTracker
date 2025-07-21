package com.RoomyExpense.tracker.controller;

import com.RoomyExpense.tracker.DTO.UserCreationDTO;
import com.RoomyExpense.tracker.DTO.UserDTO;
import com.RoomyExpense.tracker.DTO.UserRoleUpdateDTO;
import com.RoomyExpense.tracker.DTO.UserUpdateDTO;
import com.RoomyExpense.tracker.model.User;
import com.RoomyExpense.tracker.service.IHouseService;
import com.RoomyExpense.tracker.service.IUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User Management", description = "Operations related to User management")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IHouseService houseService;

    @Operation(
        summary = "Create a new user",
        description = "Creates a new user with the provided information."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created successfully",
            content = @Content(schema = @Schema(implementation = UserDTO.class))),
        @ApiResponse(responseCode = "404", description = "Related entity not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "User creation data",
                required = true,
                content = @Content(schema = @Schema(implementation = UserCreationDTO.class))
            )
            @Valid @RequestBody UserCreationDTO userCreationDTO) {
        try {
            UserDTO userDTO = userService.createUser(userCreationDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while creating User.");
        }
    }

    @Operation(
        summary = "Get all users",
        description = "Returns a list of all registered users."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of users or message if empty",
            content = @Content(schema = @Schema(implementation = UserDTO.class)))
    })
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllUsers() {
        List<UserDTO> userDTOs = userService.getAllUsers();

        if (userDTOs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("There are no Users currently registered.");
        }

        return ResponseEntity.ok(userDTOs);
    }

    @Operation(
        summary = "Get user by ID",
        description = "Returns a user by their ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found",
            content = @Content(schema = @Schema(implementation = UserDTO.class))),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getUserById(
            @Parameter(description = "ID of the user to retrieve", required = true)
            @PathVariable Long id) {
        Optional<UserDTO> userDTOOptional = userService.getUserById(id);

        if (userDTOOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with ID " + id + " not found.");
        }

        return ResponseEntity.ok(userDTOOptional.get());
    }

    @Operation(
        summary = "Delete user by ID",
        description = "Deletes a user by their ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<?> deleteUserById(
            @Parameter(description = "ID of the user to delete", required = true)
            @PathVariable Long id) {
        return userService.getUserById(id)
                .map(userDTO -> {
                    userService.deleteUser(id);
                    return ResponseEntity.status(HttpStatus.OK).body("User with ID " + id + " successfully deleted.");
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with ID " + id + " not found."));
    }

    @Operation(
        summary = "Update user",
        description = "Updates user information by ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully",
            content = @Content(schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PatchMapping("/updateUser/{userId}")
    public ResponseEntity<User> updateUser(
            @Parameter(description = "ID of the user to update", required = true)
            @PathVariable Long userId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "User update data",
                required = true,
                content = @Content(schema = @Schema(implementation = UserUpdateDTO.class))
            )
            @RequestBody UserUpdateDTO userUpdateDTO) {

        User updatedUser = userService.updateUser(userId, userUpdateDTO);

        return ResponseEntity.ok(updatedUser);
    }

    @Operation(
        summary = "Change user role",
        description = "Changes the role of a user by ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User role updated successfully",
            content = @Content(schema = @Schema(implementation = UserDTO.class))),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PatchMapping("/{userId}/role")
    public ResponseEntity<UserDTO> changeUserRole(
            @Parameter(description = "ID of the user to update role", required = true)
            @PathVariable Long userId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Role update data",
                required = true,
                content = @Content(schema = @Schema(implementation = UserRoleUpdateDTO.class))
            )
            @Valid @RequestBody UserRoleUpdateDTO updateRoleDTO) {

        UserDTO updatedUser = userService.changeUserRole(userId, updateRoleDTO);
        return ResponseEntity.ok(updatedUser);
    }

}
