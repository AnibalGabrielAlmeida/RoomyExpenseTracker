package com.RoomyExpense.tracker.controller;

import com.RoomyExpense.tracker.DTO.ExpenseSplitCreationDTO;
import com.RoomyExpense.tracker.DTO.ExpenseSplitDTO;
import com.RoomyExpense.tracker.DTO.ExpenseSplitUpdateDTO;
import com.RoomyExpense.tracker.model.ExpenseSplit;
import com.RoomyExpense.tracker.service.ExpenseSplitService;
import jakarta.persistence.EntityNotFoundException;
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
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/expense-splits")
@Tag(name = "Expense Split Management", description = "Operations related to Expense Split management")
public class ExpenseSplitController {

    @Autowired
    private ExpenseSplitService expenseSplitService;

    @Operation(
        summary = "Create a new expense split",
        description = "Creates a new expense split with the provided information."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Expense split created successfully",
            content = @Content(schema = @Schema(implementation = ExpenseSplit.class))),
        @ApiResponse(responseCode = "404", description = "Related entity not found"),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/createExpenseSplit")
    public ResponseEntity<?> createExpenseSplit(
            @RequestBody(
                description = "Expense split creation data",
                required = true,
                content = @Content(schema = @Schema(implementation = ExpenseSplitCreationDTO.class))
            )
            @org.springframework.web.bind.annotation.RequestBody ExpenseSplitCreationDTO expenseSplitCreationDTO) {
        try {
            List<ExpenseSplit> expenseSplits = expenseSplitService.createExpenseSplit(expenseSplitCreationDTO);
            StringBuilder response = new StringBuilder();

            for (ExpenseSplit expenseSplit : expenseSplits) {
                response.append("Expense Split created. ")
                        .append("\nUser: ").append(expenseSplit.getUser().getName())
                        .append("\nExpense: ").append(expenseSplit.getExpense().getName())
                        .append("\nAmount split: ").append(expenseSplit.getAmount()).append("\n");
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(response.toString());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred while creating the Expense split");
        }
    }

    @Operation(
        summary = "Get all expense splits",
        description = "Returns a list of all registered expense splits."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of expense splits or message if empty",
            content = @Content(schema = @Schema(implementation = ExpenseSplitDTO.class)))
    })
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllExpenseSplits() {
        List<ExpenseSplitDTO> expenseSplitDTOS = expenseSplitService.getAllExpenseSplits();
        if (expenseSplitDTOS.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body("There are no Expense splits registered at the moment");
        }
        return ResponseEntity.ok(expenseSplitDTOS);
    }

    @Operation(
        summary = "Get expense split by ID",
        description = "Returns an expense split by its ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Expense split found",
            content = @Content(schema = @Schema(implementation = ExpenseSplitDTO.class))),
        @ApiResponse(responseCode = "404", description = "Expense split not found")
    })
    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getExpenseSplitById(
            @Parameter(description = "ID of the expense split to retrieve", required = true)
            @PathVariable Long id) {
        Optional<ExpenseSplitDTO> expenseSplitDTO = expenseSplitService.getExpenseSplitById(id);
        if (expenseSplitDTO.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Expense Split not found");
        }
        return ResponseEntity.ok(expenseSplitDTO.get());
    }

    @Operation(
        summary = "Delete expense split by ID",
        description = "Deletes an expense split by its ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Expense split deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Expense split not found")
    })
    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<?> deleteExpenseSplit(
            @Parameter(description = "ID of the expense split to delete", required = true)
            @PathVariable Long id){
        return expenseSplitService.getExpenseSplitById(id)
                .map(expenseSplitDTO -> {expenseSplitService.deleteExpenseSplit(id);
                return ResponseEntity.status(HttpStatus.OK).body("Expense Split with Id: " + id + " Succesfully eliminated.");
                }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Expense Split with Id: " + id + " not found"));
    }

    @Operation(
        summary = "Update expense split",
        description = "Updates expense split information by ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Expense split updated successfully",
            content = @Content(schema = @Schema(implementation = ExpenseSplit.class))),
        @ApiResponse(responseCode = "404", description = "Expense split not found"),
        @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PatchMapping("/updateExpenseSplit/{expenseSplitId}")
    public ResponseEntity<ExpenseSplit> updateExpenseSplit(
            @Parameter(description = "ID of the expense split to update", required = true)
            @PathVariable Long expenseSplitId,
            @RequestBody(
                description = "Expense split update data",
                required = true,
                content = @Content(schema = @Schema(implementation = ExpenseSplitUpdateDTO.class))
            )
            @org.springframework.web.bind.annotation.RequestBody ExpenseSplitUpdateDTO expenseSplitUpdateDTO) {
        try {
            ExpenseSplit updatedExpenseSplit = expenseSplitService.updateExpenseSplit(expenseSplitId, expenseSplitUpdateDTO);
            return ResponseEntity.ok(updatedExpenseSplit);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
