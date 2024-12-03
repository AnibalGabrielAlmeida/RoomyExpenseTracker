package com.RoomyExpense.tracker.service;

import com.RoomyExpense.tracker.DTO.ExpenseShareCreationDTO;
import com.RoomyExpense.tracker.model.Expense;
import com.RoomyExpense.tracker.model.ExpenseShare;
import com.RoomyExpense.tracker.model.User;
import com.RoomyExpense.tracker.repository.ExpenseRepository;
import com.RoomyExpense.tracker.repository.ExpenseShareRepository;
import com.RoomyExpense.tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class ExpenseShareService {

    @Autowired
    private ExpenseShareRepository expenseShareRepository;
    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private UserRepository userRepository;

    public ExpenseShare createExpenseShare(ExpenseShareCreationDTO dto) {
        Expense expense = expenseRepository.findById(dto.getExpenseId())
                .orElseThrow(() -> {
                    return new NoSuchElementException("Expense not found");
                });
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        double sharedAmount = dto.getTotalAmount() * (dto.getDivisionPercentage() / 100);

        ExpenseShare expenseShare = new ExpenseShare();
        expenseShare.setExpense(expense);
        expenseShare.setUser(user);
        expenseShare.setTotalAmount(dto.getTotalAmount());
        expenseShare.setDivisionPercentage(dto.getDivisionPercentage());
        expenseShare.setSharedAmount(sharedAmount);

        return expenseShareRepository.save(expenseShare);
    }

}

