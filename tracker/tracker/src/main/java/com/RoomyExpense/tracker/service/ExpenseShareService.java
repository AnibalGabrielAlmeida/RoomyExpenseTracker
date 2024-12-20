package com.RoomyExpense.tracker.service;

import com.RoomyExpense.tracker.DTO.ExpenseShareCreationDTO;
import com.RoomyExpense.tracker.model.Expense;
import com.RoomyExpense.tracker.model.ExpenseShare;
import com.RoomyExpense.tracker.model.User;
import com.RoomyExpense.tracker.repository.ExpenseRepository;
import com.RoomyExpense.tracker.repository.ExpenseShareRepository;
import com.RoomyExpense.tracker.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExpenseShareService {

    @Autowired
    private ExpenseShareRepository expenseShareRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;
//todo this implementation.
    public ExpenseShare createExpenseShare(ExpenseShareCreationDTO dto) {
        // Buscar la expensa por su ID
        Expense expense = expenseRepository.findById(dto.getExpenseId())
                .orElseThrow(() -> new EntityNotFoundException("Expense not found with ID: " + dto.getExpenseId()));

        // Buscar el usuario por su ID
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + dto.getUserId()));

        // Calcular la cantidad compartida por el usuario
        double sharedAmount = dto.getTotalAmount() * (dto.getDivisionPercentage() / 100);

        // Crear y guardar el objeto ExpenseShare
        ExpenseShare expenseShare = new ExpenseShare();
        expenseShare.setExpense(expense);
        expenseShare.setUser(user);
        expenseShare.setTotalAmount(dto.getTotalAmount());
        expenseShare.setDivisionPercentage(dto.getDivisionPercentage());
        expenseShare.setSharedAmount(sharedAmount);

        return expenseShareRepository.save(expenseShare);
    }
}
