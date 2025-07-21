package com.RoomyExpense.tracker.controller;

import com.RoomyExpense.tracker.DTO.HouseCreationDTO;
import com.RoomyExpense.tracker.DTO.HouseDTO;
import com.RoomyExpense.tracker.DTO.UserDTO;
import com.RoomyExpense.tracker.service.IHouseService;
import com.RoomyExpense.tracker.service.IUserService;
import com.RoomyExpense.tracker.model.House;
import com.RoomyExpense.tracker.DTO.HouseUpdateDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/house")
@Tag(name = "House Management", description = "Operations related to House management")
public class HouseController {

    @Autowired
    private IHouseService houseService;
    @Autowired
    private IUserService userService;

    @Operation(
        summary = "Create a new house",
        description = "Creates a new house with the provided information."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "House created successfully",
            content = @Content(schema = @Schema(implementation = HouseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/createHouse")
    public ResponseEntity<?> createHouse(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "House creation data",
                required = true,
                content = @Content(schema = @Schema(implementation = HouseCreationDTO.class))
            )
            @RequestBody @Valid HouseCreationDTO houseCreationDTO) {
        try {
            HouseDTO houseDTO = houseService.createHouse(houseCreationDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(houseDTO);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while creating the house");
        }
    }

    @Operation(
        summary = "Get all houses",
        description = "Returns a list of all registered houses."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of houses or message if empty",
            content = @Content(schema = @Schema(implementation = HouseDTO.class)))
    })
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllHouses() {
        List<HouseDTO> houseDTOs = houseService.getAllHouses();
        if (houseDTOs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("There are no houses currently registered.");
        }
        return ResponseEntity.ok(houseDTOs);
    }

    @Operation(
        summary = "Get house by ID",
        description = "Returns a house by its ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "House found",
            content = @Content(schema = @Schema(implementation = HouseDTO.class))),
        @ApiResponse(responseCode = "404", description = "House not found")
    })
    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getHouseById(
            @Parameter(description = "ID of the house to retrieve", required = true)
            @PathVariable Long id) {
        Optional<HouseDTO> houseDTO = houseService.getHouseById(id);
        if (houseDTO.isPresent()) {
            return ResponseEntity.ok(houseDTO.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("House with ID " + id + " not found.");
        }
    }

    @Operation(
        summary = "Delete house by ID",
        description = "Deletes a house by its ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "House deleted successfully"),
        @ApiResponse(responseCode = "404", description = "House not found")
    })
    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<?> deleteHouseById(
            @Parameter(description = "ID of the house to delete", required = true)
            @PathVariable Long id) {
        return houseService.getHouseById(id)
                .map(houseDTO -> {
                    houseService.deleteHouse(id);
                    return ResponseEntity.status(HttpStatus.OK).body("House with ID " + id + " successfully deleted.");
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("House with ID " + id + " not found."));
    }

    @Operation(
        summary = "Update house",
        description = "Updates house information by ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "House updated successfully",
            content = @Content(schema = @Schema(implementation = House.class))),
        @ApiResponse(responseCode = "404", description = "House not found")
    })
    @PatchMapping("/updateHouse/{houseId}")
    public ResponseEntity<House> updateHouse(
            @Parameter(description = "ID of the house to update", required = true)
            @PathVariable Long houseId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "House update data",
                required = true,
                content = @Content(schema = @Schema(implementation = HouseUpdateDTO.class))
            )
            @RequestBody HouseUpdateDTO houseUpdateDTO) {

        House updatedHouse = houseService.updateHouse(houseId, houseUpdateDTO);

        return ResponseEntity.ok(updatedHouse);
    }

    @Operation(
        summary = "Get roommates by house ID",
        description = "Returns a list of roommates for a given house."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of roommates or message if empty",
            content = @Content(schema = @Schema(implementation = UserDTO.class)))
    })
    @GetMapping("/{houseId}/roommates")
    public ResponseEntity<?> getRoommates(
            @Parameter(description = "ID of the house", required = true)
            @PathVariable Long houseId) {
        List<UserDTO> roommatesDTOs = houseService.getRoommatesByHouseId(houseId);
        if (roommatesDTOs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("There are no users associated to this house.");
        }
        return ResponseEntity.ok(roommatesDTOs);
    }

    @Operation(
        summary = "Add existing user to house",
        description = "Adds an existing user as a roommate to a house."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User added to house successfully",
            content = @Content(schema = @Schema(implementation = HouseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PatchMapping("/{houseId}/addRoommate/{userId}")
    public ResponseEntity<HouseDTO> addExistingUserToHouse(
            @Parameter(description = "ID of the house", required = true)
            @PathVariable Long houseId,
            @Parameter(description = "ID of the user to add", required = true)
            @PathVariable Long userId) {
        try {
            HouseDTO updatedHouse = houseService.addExistingUserToHouse(houseId, userId);
            return ResponseEntity.ok(updatedHouse);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @Operation(
        summary = "Remove user from house",
        description = "Removes a user from a house."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User removed from house successfully",
            content = @Content(schema = @Schema(implementation = HouseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "404", description = "User or house not found")
    })
    @PatchMapping("/{houseId}/removeRoommate/{userId}")
    public ResponseEntity<HouseDTO> removeUserFromHouse(
            @Parameter(description = "ID of the house", required = true)
            @PathVariable Long houseId,
            @Parameter(description = "ID of the user to remove", required = true)
            @PathVariable Long userId) {
        try {
            HouseDTO houseDTO = houseService.removeUserFromHouse(houseId, userId);
            return ResponseEntity.ok(houseDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
