package com.RoomyExpense.tracker.mapper;

import com.RoomyExpense.tracker.DTO.ExpenseCreationDTO;
import com.RoomyExpense.tracker.model.Expense;
import org.springframework.stereotype.Component;

@Component
public class ExpenseMapper {
    public Expense toEntity(ExpenseCreationDTO expenseCreationDTO){
        Expense expense = new Expense();

        expense.setName(expenseCreationDTO.getName());
        expense.setDescription(expenseCreationDTO.getDescription());
        expense.setAmount(expenseCreationDTO.getAmount());
        expense.setCategory(Expense.Category.valueOf(expenseCreationDTO.getCategory()));
        expense.setDate(expenseCreationDTO.getDate());

        return expense;
    }

}
