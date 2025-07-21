package com.RoomyExpense.tracker.controller;

import com.RoomyExpense.tracker.DTO.ExpenseCreationDTO;
import com.RoomyExpense.tracker.DTO.ExpenseUpdateDTO;
import com.RoomyExpense.tracker.model.Expense;
import com.RoomyExpense.tracker.service.IExpenseService;
import com.RoomyExpense.tracker.DTO.ExpenseDTO;
import com.RoomyExpense.tracker.DTO.HouseDTO;
import com.RoomyExpense.tracker.service.IHouseService;
import com.RoomyExpense.tracker.model.House;
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
@RequestMapping("/api/expenses")
@Tag(name = "Expense Management", description = "Operations related to Expense management")
public class ExpenseController {

    @Autowired
    private IExpenseService expenseService;

    @Autowired
    private IHouseService houseService;

    @Operation(
        summary = "Create a new expense",
        description = "Creates a new expense with the provided information."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Expense created successfully",
            content = @Content(schema = @Schema(implementation = Expense.class))),
        @ApiResponse(responseCode = "404", description = "Related entity not found"),
        @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("/createExpense")
    public ResponseEntity<?> createExpense(
            @RequestBody(
                description = "Expense creation data",
                required = true,
                content = @Content(schema = @Schema(implementation = ExpenseCreationDTO.class))
            )
            @org.springframework.web.bind.annotation.RequestBody ExpenseCreationDTO expenseCreationDTO) {
        try {
            Expense expense = expenseService.createExpense(expenseCreationDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Expense successfully created. House: " + expense.getHouse().getName());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while creating  the expense.");
        }
    }

    @Operation(
        summary = "Get all expenses",
        description = "Returns a list of all registered expenses."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of expenses",
            content = @Content(schema = @Schema(implementation = ExpenseDTO.class)))
    })
    @GetMapping("/getAll")
    public ResponseEntity<List<ExpenseDTO>> getAllExpenses() {
        List<ExpenseDTO> expenseDTOs = expenseService.getAllExpenses();
        return ResponseEntity.ok(expenseDTOs); 
    }

    @Operation(
        summary = "Get expense by ID",
        description = "Returns an expense by its ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Expense found",
            content = @Content(schema = @Schema(implementation = ExpenseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Expense not found")
    })
    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getExpenseById(
            @Parameter(description = "ID of the expense to retrieve", required = true)
            @PathVariable Long id) {
        Optional<ExpenseDTO> expenseDTO = expenseService.getExpenseById(id);

        if (expenseDTO.isPresent()) {
            return ResponseEntity.ok(expenseDTO.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Expense with ID " + id + " not found.");
        }
    }

    @Operation(
        summary = "Delete expense by ID",
        description = "Deletes an expense by its ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Expense deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Expense not found")
    })
    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<?> deleteExpenseById(
            @Parameter(description = "ID of the expense to delete", required = true)
            @PathVariable Long id) {
        return expenseService.getExpenseById(id)
                .map(expenseDTO -> {
                    expenseService.deleteExpense(id);
                    return ResponseEntity.status(HttpStatus.OK).body("Expense with ID " + id + " successfully deleted.");
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Expense with ID " + id + " not found."));
    }

    @Operation(
        summary = "Update expense",
        description = "Updates expense information by ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Expense updated successfully",
            content = @Content(schema = @Schema(implementation = Expense.class))),
        @ApiResponse(responseCode = "404", description = "Expense not found")
    })
    @PatchMapping("/updateExpense/{expenseId}")
    public ResponseEntity<Expense> updateExpense(
            @Parameter(description = "ID of the expense to update", required = true)
            @PathVariable Long expenseId,
            @RequestBody(
                description = "Expense update data",
                required = true,
                content = @Content(schema = @Schema(implementation = ExpenseUpdateDTO.class))
            )
            @org.springframework.web.bind.annotation.RequestBody ExpenseUpdateDTO expenseUpdateDTO) {

        Expense updatedExpense = expenseService.updateExpense(expenseId, expenseUpdateDTO);

        return ResponseEntity.ok(updatedExpense);
    }

    /*
    @Operation(
        summary = "Get expenses by house ID",
        description = "Returns a list of expenses for a given house."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of expenses for the house",
            content = @Content(schema = @Schema(implementation = ExpenseDTO.class))),
        @ApiResponse(responseCode = "404", description = "House not found")
    })
    @GetMapping("/getByHouse/{houseId}")
    public ResponseEntity<?> getExpensesByHouse(
            @Parameter(description = "ID of the house", required = true)
            @PathVariable Long houseId) {
        Optional<HouseDTO> house = houseService.getHouseById(houseId);

        if (!house.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Casa no encontrada.");
        }

        List<Expense> expenses = expenseService.getExpensesByHouseId(house.get().getId());

        if (expenses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("No hay gastos registrados para esta casa.");
        }

        List<ExpenseDTO> expenseDTOs = expenses.stream()
                .map(expense -> new ExpenseDTO(
                        expense.getId(),
                        expense.getName(),
                        expense.getDescription(),
                        expense.getAmount(),
                        expense.getCategory().toString(),
                        expense.getDate(),
                        expense.getHouse().getName(),
                        expense.getHouse().getId()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(expenseDTOs);
    }
    */
}
