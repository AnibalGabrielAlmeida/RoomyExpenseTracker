package com.RoomyExpense.tracker.controller;

import com.RoomyExpense.tracker.DTO.HouseCreationDTO;
import com.RoomyExpense.tracker.DTO.HouseDTO;
import com.RoomyExpense.tracker.DTO.UserDTO;
import com.RoomyExpense.tracker.service.IHouseService;
import com.RoomyExpense.tracker.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/house")
public class HouseController {

    @Autowired
    private IHouseService houseService;
    @Autowired
    private IUserService userService;


    @PostMapping("/createHouse")
    public ResponseEntity<?> createHouse(@RequestBody @Valid HouseCreationDTO houseCreationDTO) {
        try {
            HouseDTO houseDTO = houseService.createHouse(houseCreationDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(houseDTO);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while creating the house");
        }

    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllHouses() {
        List<HouseDTO> houseDTOs = houseService.getAllHouses();
        if (houseDTOs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("There are no houses currently registered.");
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
                    .body("House with ID " + id + " not found.");
        }
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<?> deleteHouseById(@PathVariable Long id) {
        return houseService.getHouseById(id)
                .map(houseDTO -> {
                    houseService.deleteHouse(id);
                    return ResponseEntity.status(HttpStatus.OK).body("House with ID " + id + " successfully deleted.");
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("House with ID " + id + " not found."));
    }

    @GetMapping("/{houseId}/roommates")
    public ResponseEntity<?> getRoommates(@PathVariable Long houseId) {
        List<UserDTO> roommatesDTOs = houseService.getRoommatesByHouseId(houseId);
        if (roommatesDTOs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("There are no users associated to this house.");
        }
        return ResponseEntity.ok(roommatesDTOs);
    }

    @PatchMapping("/{houseId}/addRoommate/{userId}")
    public ResponseEntity<HouseDTO> addExistingUserToHouse(
            @PathVariable Long houseId,
            @PathVariable Long userId) {
        try {
            HouseDTO updatedHouse = houseService.addExistingUserToHouse(houseId, userId);
            return ResponseEntity.ok(updatedHouse);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PatchMapping("/{houseId}/removeRoommate/{userId}")
    public ResponseEntity<HouseDTO> removeUserFromHouse(
            @PathVariable Long houseId,
            @PathVariable Long userId) {
        try {
            HouseDTO houseDTO = houseService.removeUserFromHouse(houseId, userId);
            return ResponseEntity.ok(houseDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // O puedes manejar el error de otra manera
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // o BAD_REQUEST según sea el caso
        }
    }

}
