package com.RoomyExpense.tracker.service;


import com.RoomyExpense.tracker.DTO.ExpenseDTO;
import com.RoomyExpense.tracker.DTO.ExpenseSplitCreationDTO;
import com.RoomyExpense.tracker.DTO.ExpenseSplitDTO;
import com.RoomyExpense.tracker.DTO.ExpenseSplitUpdateDTO;
import com.RoomyExpense.tracker.exception.ExpenseNotFoundException;
import com.RoomyExpense.tracker.exception.ExpenseSplitNotFoundException;
import com.RoomyExpense.tracker.exception.UserNotFoundException;
import com.RoomyExpense.tracker.mapper.ExpenseSplitMapper;
import com.RoomyExpense.tracker.model.Expense;
import com.RoomyExpense.tracker.model.ExpenseSplit;
import com.RoomyExpense.tracker.model.User;
import com.RoomyExpense.tracker.repository.ExpenseRepository;
import com.RoomyExpense.tracker.repository.ExpenseSplitRepository;
import com.RoomyExpense.tracker.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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


    @Transactional
    @Override
    public List<ExpenseSplit> createExpenseSplit(ExpenseSplitCreationDTO expenseSplitCreationDTO) {
        Optional<ExpenseDTO> expenseOptional = expenseService.getExpenseById(expenseSplitCreationDTO.getExpenseId());
        if (expenseOptional.isEmpty()) {
            throw new ExpenseNotFoundException("Expense with ID " + expenseSplitCreationDTO.getExpenseId() + " not found");
        }

        Expense expense = expenseRepository.findById(expenseOptional.get().getId())
                .orElseThrow(() -> new ExpenseNotFoundException("Expense is not registered in the DB"));

        Double totalAmount = expense.getAmount();
        if (totalAmount == null || totalAmount <= 0) {
            throw new IllegalStateException("Expense must have a valid total amount");
        }

        // Calculate shared amount using streams
        double sharedAmount = expenseSplitCreationDTO.getUserPercentages().stream()
                .mapToDouble(userPercentageDTO -> totalAmount * (userPercentageDTO.getDivisionPercentage() / 100))
                .sum();

        // Ensure that 100% is distributed correctly
        if (sharedAmount != totalAmount) {
            throw new IllegalStateException("Total division percentage must be 100%");
        }

        // Create the ExpenseSplit in a single flow
        List<ExpenseSplit> expenseSplits = expenseSplitCreationDTO.getUserPercentages().stream()
                .map(userPercentageDTO -> {
                    User user = userRepository.findById(userPercentageDTO.getUserId())
                            .orElseThrow(() -> new UserNotFoundException("User is not registered in the DB"));
                    ExpenseSplit expenseSplit = new ExpenseSplit();
                    expenseSplit.setUser(user);
                    expenseSplit.setExpense(expense);
                    expenseSplit.setAmount(totalAmount * (userPercentageDTO.getDivisionPercentage() / 100));
                    return expenseSplit;
                })
                .collect(Collectors.toList());

        return expenseSplitRepository.saveAll(expenseSplits);
    }

    @Override
    public List<ExpenseSplitDTO> getAllExpenseSplits() {
        List<ExpenseSplit> expenseSplits = expenseSplitRepository.findAll();
        return expenseSplits.stream()
                .map(expenseSplitMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ExpenseSplitDTO> getExpenseSplitById(Long id) {

        ExpenseSplit expenseSplit = expenseSplitRepository.findById(id)
                .orElseThrow(() -> new ExpenseSplitNotFoundException("ExpenseSplit with ID " + id + " not found"));
        return Optional.of(expenseSplitMapper.toDTO(expenseSplit));
    }

    @Transactional
    @Override
    public void deleteExpenseSplit(Long id) {

        if (!expenseSplitRepository.existsById(id)) {
            throw new ExpenseSplitNotFoundException("ExpenseSplit with ID " + id + " not found");
        }
        expenseSplitRepository.deleteById(id);
    }

    @Transactional
    @Override
    public ExpenseSplit updateExpenseSplit(Long expenseSplitId, ExpenseSplitUpdateDTO expenseSplitUpdateDTO) {
        // Get the ExpenseSplit to update
        ExpenseSplit expenseSplit = expenseSplitRepository.findById(expenseSplitId)
                .orElseThrow(() -> new ExpenseSplitNotFoundException("ExpenseSplit with ID: " + expenseSplitId + " not found"));

        // Get all ExpenseSplits related to the same Expense
        List<ExpenseSplit> allSplits = expenseSplitRepository.findByExpenseId(expenseSplit.getExpense().getId());

        // Validate that there is more than one split (there is no point in updating if there is only one)
        if (allSplits.size() <= 1) {
            throw new IllegalStateException("Cannot update a split when there is only one associated split.");
        }

        // Update the split amount or percentage
        if (expenseSplitUpdateDTO.getAmount() != null) {
            expenseSplit.setAmount(expenseSplitUpdateDTO.getAmount());
        }

        // Recalculate the rest of the splits
        double updatedAmount = expenseSplit.getAmount();
        double totalAmount = expenseSplit.getExpense().getAmount();

        // Validate that the new amount does not exceed the total
        if (updatedAmount > totalAmount) {
            throw new IllegalStateException("Updated amount exceeds the total expense amount.");
        }

        // Calculate how much is left to distribute among the other splits
        double remainingAmount = totalAmount - updatedAmount;

        // Update the other splits proportionally
        List<ExpenseSplit> otherSplits = allSplits.stream()
                .filter(split -> !split.getId().equals(expenseSplitId))
                .collect(Collectors.toList());

        double totalOtherAmounts = otherSplits.stream().mapToDouble(ExpenseSplit::getAmount).sum();

        for (ExpenseSplit otherSplit : otherSplits) {
            double newAmount = (otherSplit.getAmount() / totalOtherAmounts) * remainingAmount;
            otherSplit.setAmount(newAmount);
        }

        // Save all changes
        expenseSplitRepository.saveAll(allSplits);

        // Save and return the updated split
        return expenseSplitRepository.save(expenseSplit);
    }
}
