package com.RoomyExpense.tracker.controller;

import com.RoomyExpense.tracker.DTO.HouseCreationDTO;
import com.RoomyExpense.tracker.DTO.HouseDTO;
import com.RoomyExpense.tracker.model.House;
import com.RoomyExpense.tracker.model.User;
import com.RoomyExpense.tracker.service.IHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/house")
public class HouseController {

    @Autowired
    IHouseService houseService;

    @PostMapping("/saveHouse")
    public ResponseEntity<HouseDTO> createHouse(@RequestBody HouseCreationDTO houseCreationDTO) {
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
}
