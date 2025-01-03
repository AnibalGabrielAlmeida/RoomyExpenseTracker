package com.RoomyExpense.tracker.service;

import com.RoomyExpense.tracker.DTO.ExpenseSplitCreationDTO;
import com.RoomyExpense.tracker.DTO.ExpenseSplitDTO;
import com.RoomyExpense.tracker.DTO.ExpenseSplitUpdateDTO;
import com.RoomyExpense.tracker.model.ExpenseSplit;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public interface IExpenseSplit {

    List<ExpenseSplit> createExpenseSplit(ExpenseSplitCreationDTO expenseSplitCreationDTO);

    List<ExpenseSplitDTO> getAllExpenseSplits();

    Optional<ExpenseSplitDTO> getExpenseSplitById(Long id);

    void deleteExpenseSplit(Long id);

    ExpenseSplit updateExpenseSplit(Long expenseSplitId, ExpenseSplitUpdateDTO expenseSplitUpdateDTO);
}
