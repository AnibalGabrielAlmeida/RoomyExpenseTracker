package com.RoomyExpense.tracker.controller;

import com.RoomyExpense.tracker.model.Expense;
import com.RoomyExpense.tracker.model.House;
import com.RoomyExpense.tracker.service.IExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/expense")
public class ExpenseController {

    @Autowired
    IExpenseService expenseService;

    //add Response Entity to controllers
    @GetMapping("/getAll")
    public List<Expense> getAllExpenses() {
        return expenseService.getAllExpenses();
    }

    @GetMapping("/getById/{id}")
    public Optional<Expense> getExpenseByid(@PathVariable Long id) {
        return expenseService.getExpenseById(id);
    }

    @PostMapping("/saveExpense")
    public Expense saveExpense(@RequestBody Expense expense) {
        return expenseService.saveExpense(expense);
    }

    @DeleteMapping("/deleteById/{id}")
    public void deleteExpenseById(@PathVariable Long id) {
        expenseService.deleteExpense(id);
    }

}
