package com.RoomyExpense.tracker.service;

import com.RoomyExpense.tracker.DTO.ExpenseDTO;
import com.RoomyExpense.tracker.model.Expense;
import com.RoomyExpense.tracker.model.House;
import com.RoomyExpense.tracker.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ExpenseService implements IExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Override
    public List<Expense> getAllExpenses(){
        return expenseRepository.findAll();
    }


    //check optional handling
    @Override
    public Optional<Expense> getExpenseById(Long id){

        return  expenseRepository.findById(id);
    }

    @Override
    public Expense saveExpense(Expense expense){
        return expenseRepository.save(expense);
    }

    @Override
    public void deleteExpense(Long id){
        expenseRepository.deleteById(id);
    }

    @Override
    public List<Expense> getExpensesByHouse(House house) {
        return expenseRepository.findByHouse(house);
    }
}
