package com.RoomyExpense.tracker.controller;

import com.RoomyExpense.tracker.DTO.HouseCreationDTO;
import com.RoomyExpense.tracker.DTO.HouseDTO;
import com.RoomyExpense.tracker.DTO.UserDTO;
import com.RoomyExpense.tracker.model.House;
import com.RoomyExpense.tracker.model.User;
import com.RoomyExpense.tracker.service.IHouseService;
import com.RoomyExpense.tracker.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/house")
public class HouseController {

    @Autowired
    IHouseService houseService;

    @Autowired
    IUserService userService;


    @PostMapping("/saveHouse")
public ResponseEntity<HouseDTO> createHouse(@RequestBody @Valid HouseCreationDTO houseCreationDTO) {
    HouseDTO houseDTO = houseService.createHouse(houseCreationDTO);
    return ResponseEntity.ok(houseDTO);
}

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllHouses() {
        List<HouseDTO> houseDTOs = houseService.getAllHouses();

        if (houseDTOs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("No hay casas registradas actualmente.");
        }

        return ResponseEntity.ok(houseDTOs);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getHouseById(@PathVariable Long id) {
        Optional<HouseDTO> houseDTO = houseService.getHouseById(id);

        if (houseDTO.isPresent()) {
            return ResponseEntity.ok(houseDTO.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Casa con ID " + id + " no encontrada.");
        }
    }






    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<?> deleteHouseById(@PathVariable Long id) {
        return houseService.getHouseById(id)
                .map(houseDTO -> {
                    houseService.deleteHouse(id);
                    return ResponseEntity.status(HttpStatus.OK).body("Casa con ID " + id + " eliminada exitosamente.");
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Casa con ID " + id + " no encontrada."));
    }

/*
    @PostMapping("/addExistingUser/{houseId}/{userId}")
    public ResponseEntity<?> addExistingUserToHouse(@PathVariable Long houseId, @PathVariable Long userId) {
        Optional<House> houseOptional = houseService.getHouseById(houseId);

        if (houseOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Casa con ID " + houseId + " no encontrada.");
        }

        Optional<User> userOptional = userService.getUserById(userId);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario con ID " + userId + " no encontrado.");
        }

        House house = houseOptional.get();
        User user = userOptional.get();

        // Verificar si el usuario ya está asignado a una casa
        if (user.getHouse() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El usuario ya está asignado a una casa.");
        }

        // Establecer la relación bidireccional
        user.setHouse(house);
        house.getRoommates().add(user);

        // Guardar cambios
        userService.saveUser(user);
        houseService.saveHouse(house);

        return ResponseEntity.status(HttpStatus.OK).body("Usuario agregado exitosamente a la casa.");
    }
*/

    @GetMapping("/{houseId}/roommates")
    public ResponseEntity<?> getRoommates(@PathVariable Long houseId) {
        List<User> roommates = houseService.getRoommatesByHouseId(houseId);

        if (roommates.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("No hay usuarios asociados a esta casa.");
        }

        // Convertir a DTOs
        List<UserDTO> roommatesDTOs = roommates.stream()
                .map(user -> new UserDTO(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getPhoneNumber(),
                        user.getRegistrationDate(),
                        user.getHouse() != null ? user.getHouse().getName() : null, // Nombre de la casa
                        user.getHouse() != null ? "/api/house/" + user.getHouse().getId() : null, // URL de la casa
                        user.getRole().name()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(roommatesDTOs);
    }
/*

    @DeleteMapping("/removeUser/{houseId}/{userId}")
    public ResponseEntity<?> removeUserFromHouse(@PathVariable Long houseId, @PathVariable Long userId) {
        Optional<House> houseOptional = houseService.getHouseById(houseId);

        if (houseOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Casa con ID " + houseId + " no encontrada.");
        }

        House house = houseOptional.get();

        // Buscar al usuario en la lista de roommates
        Optional<User> userOptional = house.getRoommates().stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst();

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario con ID " + userId + " no encontrado en la casa.");
        }

        User user = userOptional.get();

        // Eliminar al usuario de la lista de roommates de la casa
        house.getRoommates().remove(user);

        // Desasociar la casa del usuario
        user.setHouse(null);

        // Guardar los cambios
        houseService.saveHouse(house);
        userService.saveUser(user); // Necesitas un servicio que guarde el usuario

        return ResponseEntity.status(HttpStatus.OK).body("Usuario eliminado exitosamente de la casa.");
    }
*/

}
