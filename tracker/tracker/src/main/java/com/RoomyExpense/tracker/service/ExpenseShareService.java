package com.RoomyExpense.tracker.service;

import com.RoomyExpense.tracker.model.Expense;
import com.RoomyExpense.tracker.model.ExpenseShare;
import com.RoomyExpense.tracker.model.User;
import com.RoomyExpense.tracker.repository.ExpenseShareRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ExpenseShareService {
/*
    @Autowired
    private ExpenseShareRepository expenseShareRepository;

    public void createExpenseShares(Expense expense, Map<User, Double> userShares) {
        userShares.forEach((user, percentage) -> {
            ExpenseShare expenseShare = new ExpenseShare();
            expenseShare.setExpense(expense);
            expenseShare.setUser(user);
            expenseShare.setSharePercentage(percentage);
            expenseShare.setAmountToPay(expense.getAmount() * (percentage / 100));

            // Guardar en la base de datos
            expenseShareRepository.save(expenseShare);
        });
    }*/
}

