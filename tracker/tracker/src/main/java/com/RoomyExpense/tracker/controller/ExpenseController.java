package com.RoomyExpense.tracker.controller;

import com.RoomyExpense.tracker.DTO.ExpenseCreationDTO;
import com.RoomyExpense.tracker.DTO.ExpenseUpdateDTO;
import com.RoomyExpense.tracker.model.Expense;
import com.RoomyExpense.tracker.service.IExpenseService;
import com.RoomyExpense.tracker.DTO.ExpenseDTO;
import com.RoomyExpense.tracker.service.IHouseService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private IExpenseService expenseService;

    @Autowired
    private IHouseService houseService;

    @PostMapping("/createExpense")
    public ResponseEntity<?> createExpense(@RequestBody ExpenseCreationDTO expenseCreationDTO) {
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

   @GetMapping("/getAll")
        public ResponseEntity<List<ExpenseDTO>> getAllExpenses() {
            List<ExpenseDTO> expenseDTOs = expenseService.getAllExpenses();
            return ResponseEntity.ok(expenseDTOs); // siempre devuelve []
        }


    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getExpenseById(@PathVariable Long id) {
        Optional<ExpenseDTO> expenseDTO = expenseService.getExpenseById(id);

        if (expenseDTO.isPresent()) {
            return ResponseEntity.ok(expenseDTO.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Expense with ID " + id + " not found.");
        }
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<?> deleteExpenseById(@PathVariable Long id) {
        return expenseService.getExpenseById(id)
                .map(expenseDTO -> {
                    expenseService.deleteExpense(id);
                    return ResponseEntity.status(HttpStatus.OK).body("Expense with ID " + id + " successfully deleted.");
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Expense with ID " + id + " not found."));
    }

    @PatchMapping("/updateExpense/{expenseId}")
    public ResponseEntity<Expense> updateExpense(
            @PathVariable Long expenseId,
            @RequestBody ExpenseUpdateDTO expenseUpdateDTO) {

        Expense updatedExpense = expenseService.updateExpense(expenseId, expenseUpdateDTO);

        return ResponseEntity.ok(updatedExpense);
    }

    /*
    // Obtener gastos por ID de casa
    @GetMapping("/getByHouse/{houseId}")
    public ResponseEntity<?> getExpensesByHouse(@PathVariable Long houseId) {
        Optional<House> house = houseService.getHouseById(houseId);

        if (!house.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Casa no encontrada.");
        }

        List<Expense> expenses = expenseService.getExpensesByHouse(house.get());

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
                        expense.getHouse().getName(),  // Nombre de la casa
                        expense.getHouse().getId()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(expenseDTOs);
    }*/

}
