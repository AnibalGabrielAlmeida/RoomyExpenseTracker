package com.RoomyExpense.tracker.service;


import com.RoomyExpense.tracker.DTO.ExpenseDTO;
import com.RoomyExpense.tracker.DTO.ExpenseSplitCreationDTO;
import com.RoomyExpense.tracker.DTO.ExpenseSplitDTO;
import com.RoomyExpense.tracker.mapper.ExpenseSplitMapper;
import com.RoomyExpense.tracker.model.Expense;
import com.RoomyExpense.tracker.model.ExpenseSplit;
import com.RoomyExpense.tracker.model.User;
import com.RoomyExpense.tracker.repository.ExpenseRepository;
import com.RoomyExpense.tracker.repository.ExpenseSplitRepository;
import com.RoomyExpense.tracker.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExpenseSplitService implements IExpenseSplit {

    @Autowired
    private ExpenseSplitRepository expenseSplitRepository;
    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ExpenseService expenseService;
    @Autowired
    private ExpenseSplitMapper expenseSplitMapper;




    @Override
    public List<ExpenseSplit> createExpenseSplit(ExpenseSplitCreationDTO expenseSplitCreationDTO) {
        Optional<ExpenseDTO> expenseOptional = expenseService.getExpenseById(expenseSplitCreationDTO.getExpenseId());
        if (expenseOptional.isEmpty()) {
            throw new EntityNotFoundException("Expense not found");
        }

        Expense expense = expenseRepository.findById(expenseOptional.get().getId())
                .orElseThrow(() -> new EntityNotFoundException("Expense is not registered in the DB"));

        Double totalAmount = expense.getAmount();
        if (totalAmount == null || totalAmount <= 0) {
            throw new IllegalStateException("Expense must have a valid total amount");
        }

        // Calcular el monto compartido usando streams
        double sharedAmount = expenseSplitCreationDTO.getUserPercentages().stream()
                .mapToDouble(userPercentageDTO -> totalAmount * (userPercentageDTO.getDivisionPercentage() / 100))
                .sum();

        // Asegurar que el 100% se reparta correctamente
        if (sharedAmount != totalAmount) {
            throw new IllegalStateException("Total division percentage must be 100%");
        }

        // Crear los ExpenseSplit en un solo flujo
        List<ExpenseSplit> expenseSplits = expenseSplitCreationDTO.getUserPercentages().stream()
                .map(userPercentageDTO -> {
                    User user = userRepository.findById(userPercentageDTO.getUserId())
                            .orElseThrow(() -> new EntityNotFoundException("User is not registered in the DB"));
                    ExpenseSplit expenseSplit = new ExpenseSplit();
                    expenseSplit.setUser(user);
                    expenseSplit.setExpense(expense);
                    expenseSplit.setAmount(totalAmount * (userPercentageDTO.getDivisionPercentage() / 100));
                    return expenseSplit;
                })
                .collect(Collectors.toList());

        return expenseSplitRepository.saveAll(expenseSplits);
    }



    /*
    @Override
    public ExpenseSplit createExpenseSplit(ExpenseSplitCreationDTO expenseSplitCreationDTO) {
        Optional<UserDTO> userOptional = userService.getUserById(expenseSplitCreationDTO.getUserId());
        Optional<ExpenseDTO> expenseOptional = expenseService.getExpenseById(expenseSplitCreationDTO.getExpenseId());
        if (userOptional.isEmpty()) {
            throw new EntityNotFoundException("User not found");
        }
        if (expenseOptional.isEmpty()) {
            throw new EntityNotFoundException("Expense not found");
        }

        User user = userRepository.findById(userOptional.get().getId())
                .orElseThrow(() -> new EntityNotFoundException("User is not registered in the DB"));

        Expense expense = expenseRepository.findById(expenseOptional.get().getId())
                .orElseThrow(() -> new EntityNotFoundException("Expense is not registered in the DB"));

        Double totalAmount = expense.getAmount();
        if (totalAmount == null || totalAmount <= 0) {
            throw new IllegalStateException("Expense must have a valid total amount");
        }
        Double sharedAmount = totalAmount * (expenseSplitCreationDTO.getDivisionPercentage() / 100);

        ExpenseSplit expenseSplit = new ExpenseSplit();
        expenseSplit.setUser(user);
        expenseSplit.setExpense(expense);
        expenseSplit.setAmount(sharedAmount);

        return expenseSplitRepository.save(expenseSplit);
    }*/

    @Override
    public List<ExpenseSplitDTO> getAllExpenseSplits() {
        List<ExpenseSplit> expenseSplits = expenseSplitRepository.findAll();
        return expenseSplits.stream()
                .map(expenseSplitMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ExpenseSplitDTO> getExpenseSplitById(Long id) {

        Optional<ExpenseSplit> expenseOptional = expenseSplitRepository.findById(id);
        return expenseOptional.map(expenseSplitMapper::toDTO);
    }


}
