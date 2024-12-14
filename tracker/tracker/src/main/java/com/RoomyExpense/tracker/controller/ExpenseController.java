package com.RoomyExpense.tracker.controller;

import com.RoomyExpense.tracker.model.Expense;
import com.RoomyExpense.tracker.model.House;
import com.RoomyExpense.tracker.service.IExpenseService;
import com.RoomyExpense.tracker.DTO.ExpenseDTO;
import com.RoomyExpense.tracker.service.IHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// ExpenseController.java
@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private IExpenseService expenseService;

    @Autowired
    private IHouseService houseService;

    // Obtener todos los gastos
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllExpenses() {
        List<Expense> expenses = expenseService.getAllExpenses();

        if (expenses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("No hay gastos registrados.");
        }

        // Mapeo de Expense a ExpenseDTO
        List<ExpenseDTO> expenseDTOs = expenses.stream()
                .map(expense -> new ExpenseDTO(
                        expense.getId(),
                        expense.getName(),
                        expense.getDescription(),
                        expense.getAmount(),
                        expense.getCategory().toString(),
                        expense.getDate(),
                        expense.getHouse() != null ? expense.getHouse().getName() : null,
                        expense.getHouse() != null ? expense.getHouse().getId() : null// Nombre de la casa
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(expenseDTOs);
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
/*
    // Crear un nuevo gasto
    @PostMapping("/create")
    public ResponseEntity<?> createExpense(@RequestBody ExpenseDTO expenseDTO) {
        // Buscar la casa por ID
        Optional<House> house = houseService.getHouseById(expenseDTO.getHouseId());

        if (!house.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Casa no encontrada.");
        }

        // Crear el gasto
        Expense expense = new Expense();
        expense.setName(expenseDTO.getName());
        expense.setDescription(expenseDTO.getDescription());
        expense.setAmount(expenseDTO.getAmount());
        expense.setCategory(Expense.Category.valueOf(expenseDTO.getCategory()));
        expense.setDate(expenseDTO.getDate());
        expense.setHouse(house.get()); // Asignar la casa encontrada

        // Guardar el gasto
        expenseService.saveExpense(expense);

        // Devolver el nombre de la casa
        return ResponseEntity.status(HttpStatus.CREATED).body("Gasto creado exitosamente. Casa: " + house.get().getName());
    }

    // Eliminar un gasto
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteExpense(@PathVariable Long id) {
        Optional<Expense> expense = expenseService.getExpenseById(id);

        if (!expense.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Gasto no encontrado.");
        }

        expenseService.deleteExpense(id);
        return ResponseEntity.ok("Gasto eliminado exitosamente.");
    }*/
}
