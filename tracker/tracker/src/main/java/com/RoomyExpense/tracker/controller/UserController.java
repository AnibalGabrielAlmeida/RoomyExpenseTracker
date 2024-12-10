package com.RoomyExpense.tracker.controller;

import com.RoomyExpense.tracker.DTO.UserCreationDTO;
import com.RoomyExpense.tracker.DTO.UserDTO;
import com.RoomyExpense.tracker.model.House;
import com.RoomyExpense.tracker.model.User;
import com.RoomyExpense.tracker.service.IHouseService;
import com.RoomyExpense.tracker.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/saveUser")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserCreationDTO userCreationDTO) {
        // Verificar si la casa asociada existe
        Optional<House> houseOptional = houseService.getHouseById(userCreationDTO.getHouseId());
        if (houseOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("La casa con ID " + userCreationDTO.getHouseId() + " no existe.");
        }

        // Mapea DTO a entidad
        User user = new User();
        user.setName(userCreationDTO.getName());
        user.setEmail(userCreationDTO.getEmail());
       // String encodedPassword = passwordEncoder.encode(user.getPassword()); // Cifra la contraseña
        user.setPassword(passwordEncoder.encode(userCreationDTO.getPassword()));

        user.setPhoneNumber(userCreationDTO.getPhoneNumber());
        user.setHouse(houseOptional.get());
        user.setRole(User.UserRole.valueOf(userCreationDTO.getRole().toUpperCase()));

        user.setRegistrationDate(LocalDate.now());

        // Guardar usuario
        User savedUser = userService.saveUser(user);

        // Mapea entidad a DTO para la respuesta
        UserDTO userDTO = new UserDTO(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getPhoneNumber(),
                savedUser.getRegistrationDate(),
                savedUser.getHouse() != null ? savedUser.getHouse().getName() : null, // Nombre de la casa
                savedUser.getHouse() != null ? "/api/houses/" + savedUser.getHouse().getId() : null, // URL de la casa
                savedUser.getRole().name() // Rol como string
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userService.getAllUsers();

        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("No hay usuarios registrados actualmente.");
        }

        List<UserDTO> userDTOs = users.stream()
                .map(user -> new UserDTO(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getPhoneNumber(),
                        user.getRegistrationDate(),
                        user.getHouse() != null ? user.getHouse().getName() : null, // Nombre de la casa
                        user.getHouse() != null ? "/api/houses/" + user.getHouse().getId() : null, // URL de la casa
                        user.getRole().name() // Rol como string
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(userDTOs);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<User> userOptional = userService.getUserById(id);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Usuario con ID " + id + " no encontrado.");
        }

        User user = userOptional.get();
        UserDTO userDTO = new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRegistrationDate(),
                user.getHouse() != null ? user.getHouse().getName() : null, // Nombre de la casa
                user.getHouse() != null ? "/api/houses/" + user.getHouse().getId() : null, // URL de la casa
                user.getRole().name() // Rol como string
        );

        return ResponseEntity.ok(userDTO);
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        Optional<User> userOptional = userService.getUserById(id);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Usuario con ID " + id + " no encontrado.");
        }

        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body("Usuario con ID " + id + " eliminado exitosamente.");
    }
}
