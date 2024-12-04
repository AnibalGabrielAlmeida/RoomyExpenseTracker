package com.RoomyExpense.tracker.service;

import com.RoomyExpense.tracker.model.Expense;
import com.RoomyExpense.tracker.model.House;
import com.RoomyExpense.tracker.model.User;

import java.util.List;
import java.util.Optional;

public interface IExpenseService {
    List<Expense> getAllExpenses ();
    Optional<Expense> getExpenseById(Long id);
    void deleteExpense(Long id);
    Expense saveExpense(Expense expense);

    List<Expense> getExpensesByHouse(House house);
}
