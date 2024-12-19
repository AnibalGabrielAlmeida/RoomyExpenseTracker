package com.RoomyExpense.tracker.controller;

import com.RoomyExpense.tracker.DTO.UserCreationDTO;
import com.RoomyExpense.tracker.DTO.UserDTO;
import com.RoomyExpense.tracker.DTO.UserRoleUpdateDTO;
import com.RoomyExpense.tracker.DTO.UserUpdateDTO;
import com.RoomyExpense.tracker.model.House;
import com.RoomyExpense.tracker.model.User;
import com.RoomyExpense.tracker.service.IHouseService;
import com.RoomyExpense.tracker.service.IUserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IHouseService houseService;

    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserCreationDTO userCreationDTO) {
        try {
            UserDTO userDTO = userService.createUser(userCreationDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while creating User.");
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllUsers() {
        List<UserDTO> userDTOs = userService.getAllUsers();

        if (userDTOs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("There are no Users currently registered.");
        }

        return ResponseEntity.ok(userDTOs);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<UserDTO> userDTOOptional = userService.getUserById(id);

        if (userDTOOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with ID " + id + " not found.");
        }

        return ResponseEntity.ok(userDTOOptional.get());
    }


    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(userDTO -> {
                    userService.deleteUser(id);
                    return ResponseEntity.status(HttpStatus.OK).body("User with ID " + id + " successfully deleted.");
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with ID " + id + " not found."));
    }

    /*
    @PatchMapping("/{userId}/role")
    public ResponseEntity<UserDTO> changeUserRole(@PathVariable Long userId, @RequestBody UserRoleUpdateDTO updateRoleDTO) {
        UserDTO updatedUser = userService.changeUserRole(userId, updateRoleDTO);
        return ResponseEntity.ok(updatedUser);
    }
*/



   /* @PatchMapping("/{userId}")
    public User updateUser(@PathVariable Long userId, @RequestBody UserUpdateDTO userUpdateDTO) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        // Usar BeanUtils para copiar solo los campos no nulos
        if (userUpdateDTO.getName() != null) user.setName(userUpdateDTO.getName());
        if (userUpdateDTO.getEmail() != null) user.setEmail(userUpdateDTO.getEmail());
        if (userUpdateDTO.getPhoneNumber() != null) user.setPhoneNumber(userUpdateDTO.getPhoneNumber());
        if (userUpdateDTO.getHouseId() != null) user.setHouseId(userUpdateDTO.getHouseId());


        return userService.saveUser(user);
    }*/
}
