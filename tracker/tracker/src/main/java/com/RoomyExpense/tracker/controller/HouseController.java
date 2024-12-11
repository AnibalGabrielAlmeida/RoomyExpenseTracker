package com.RoomyExpense.tracker.controller;

import com.RoomyExpense.tracker.DTO.HouseCreationDTO;
import com.RoomyExpense.tracker.DTO.HouseDTO;
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
        // Mapea DTO a entidad
        House house = new House();
        house.setName(houseCreationDTO.getName());
        house.setAddress(houseCreationDTO.getAddress());

        // Save in DB
        House savedHouse = houseService.saveHouse(house);

        // Mapea entidad a DTO para la respuesta
        HouseDTO houseDTO = new HouseDTO(savedHouse.getId(), savedHouse.getName(), savedHouse.getAddress(), List.of());
        return ResponseEntity.ok(houseDTO);
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllHouses() {
        List<House> houses = houseService.getAllHouses();

        if (houses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("No hay casas registradas actualmente.");
        }

        List<HouseDTO> houseDTOs = houses.stream()
                .map(house -> new HouseDTO(
                        house.getId(),
                        house.getName(),
                        house.getAddress(),
                        house.getRoommates().stream()
                                .map(User::getName)
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(houseDTOs);
    }


    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getHouseById(@PathVariable Long id) {
        Optional<House> houseOptional = houseService.getHouseById(id);

        if (houseOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Casa con ID " + id + " no encontrada.");
        }

        House house = houseOptional.get();
        HouseDTO houseDTO = new HouseDTO(
                house.getId(),
                house.getName(),
                house.getAddress(),
                house.getRoommates().stream()
                        .map(User::getName)
                        .collect(Collectors.toList())
        );

        return ResponseEntity.ok(houseDTO);
    }


    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<?> deleteHouseById(@PathVariable Long id) {
        Optional<House> houseOptional = houseService.getHouseById(id);

        if (houseOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Casa con ID " + id + " no encontrada.");
        }

        houseService.deleteHouse(id);
        return ResponseEntity.status(HttpStatus.OK).body("Casa con ID " + id + " eliminada exitosamente.");
    }

    @GetMapping("/{houseId}/roommates")
    public ResponseEntity<?> getRoommates(@PathVariable Long houseId) {
        List<User> roommates = houseService.getRoommatesByHouseId(houseId);

        if (roommates.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("No hay usuarios asociados a esta casa.");
        }

        return ResponseEntity.ok(roommates);
    }

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


}
