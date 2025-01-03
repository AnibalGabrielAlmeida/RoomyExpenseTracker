package com.RoomyExpense.tracker.service;

import com.RoomyExpense.tracker.DTO.ExpenseCreationDTO;
import com.RoomyExpense.tracker.DTO.ExpenseDTO;
import com.RoomyExpense.tracker.DTO.ExpenseUpdateDTO;
import com.RoomyExpense.tracker.DTO.HouseDTO;
import com.RoomyExpense.tracker.mapper.ExpenseMapper;
import com.RoomyExpense.tracker.mapper.HouseMapper;
import com.RoomyExpense.tracker.model.Expense;
import com.RoomyExpense.tracker.model.House;
import com.RoomyExpense.tracker.repository.ExpenseRepository;
import com.RoomyExpense.tracker.repository.HouseRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExpenseService implements IExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private HouseRepository houseRepository;
    @Autowired
    private HouseService houseService;
    @Autowired
    private HouseMapper houseMapper;
    @Autowired
    private ExpenseMapper expenseMapper;

    @Override
    public List<ExpenseDTO> getAllExpenses(){
        List<Expense> expenses = expenseRepository.findAll();

        return expenses.stream()
                .map(expenseMapper::toDTO)
                .collect(Collectors.toList());
    }


    @Override
    public Optional<ExpenseDTO> getExpenseById(Long id){
        Optional<Expense> expenseOptional = expenseRepository.findById(id);
        return expenseOptional.map(expenseMapper::toDTO);
    }

    @Transactional
    @Override
    public Expense createExpense(ExpenseCreationDTO expenseCreationDTO) {
        // Validate and search the house
        Optional<HouseDTO> houseOptional = houseService.getHouseById(expenseCreationDTO.getHouseId());

        if (houseOptional.isEmpty()) {
            throw new EntityNotFoundException("House not found.");
        }

        House house = houseRepository.findById(houseOptional.get().getId())
                .orElseGet(() -> houseRepository.save(houseMapper.DTOToEntity(houseOptional.get())));


        Expense expense = expenseMapper.toEntity(expenseCreationDTO);
        expense.setHouse(house);


        return expenseRepository.save(expense);
    }



    @Transactional
    @Override
    public void deleteExpense(Long id){
        expenseRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Expense updateExpense(Long expenseId, ExpenseUpdateDTO expenseUpdateDTO) {

        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new EntityNotFoundException("Expense with ID " + expenseId + " not found"));

        if (expenseUpdateDTO.getName() != null) {
            expense.setName(expenseUpdateDTO.getName());
        }
        if (expenseUpdateDTO.getAmount() != null) {
            expense.setAmount(expenseUpdateDTO.getAmount());
        }
        if (expenseUpdateDTO.getCategory() != null) {
            expense.setCategory(Expense.Category.valueOf(expenseUpdateDTO.getCategory()));
        }
        if (expenseUpdateDTO.getDescription() != null) {
            expense.setDescription(expenseUpdateDTO.getDescription());
        }
        if (expenseUpdateDTO.getDate() != null) {
            expense.setDate(expenseUpdateDTO.getDate());
        }
        if (expense.getHouse() != null) {
            House house = houseRepository.findById(expense.getHouse().getId())
                    .orElseThrow(() -> new EntityNotFoundException("House with ID " + expense.getHouse().getId() + " not found"));
            expense.setHouse(house);
        }

        return expenseRepository.save(expense);
    }

    //todo implement this.
    @Override
    public List<Expense> getExpensesByHouse(House house) {
        return expenseRepository.findByHouse(house);
    }
}
