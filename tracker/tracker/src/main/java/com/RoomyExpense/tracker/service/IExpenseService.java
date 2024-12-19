package com.RoomyExpense.tracker.service;

import com.RoomyExpense.tracker.DTO.ExpenseCreationDTO;
import com.RoomyExpense.tracker.DTO.ExpenseDTO;
import com.RoomyExpense.tracker.model.Expense;
import com.RoomyExpense.tracker.model.House;
import com.RoomyExpense.tracker.model.User;

import java.util.List;
import java.util.Optional;

public interface IExpenseService {
    List<ExpenseDTO> getAllExpenses ();
    Optional<ExpenseDTO> getExpenseById(Long id);
    void deleteExpense(Long id);
    Expense createExpense(ExpenseCreationDTO expenseCreationDTO);

    List<Expense> getExpensesByHouse(House house);
}
