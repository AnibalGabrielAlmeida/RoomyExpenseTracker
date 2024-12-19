package com.RoomyExpense.tracker.service;

import com.RoomyExpense.tracker.DTO.ExpenseCreationDTO;
import com.RoomyExpense.tracker.DTO.ExpenseDTO;
import com.RoomyExpense.tracker.DTO.HouseDTO;
import com.RoomyExpense.tracker.mapper.ExpenseMapper;
import com.RoomyExpense.tracker.mapper.HouseMapper;
import com.RoomyExpense.tracker.model.Expense;
import com.RoomyExpense.tracker.model.House;
import com.RoomyExpense.tracker.model.User;
import com.RoomyExpense.tracker.repository.ExpenseRepository;
import com.RoomyExpense.tracker.repository.HouseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
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


    //check optional handling
    @Override
    public Optional<ExpenseDTO> getExpenseById(Long id){
        Optional<Expense> expenseOptional = expenseRepository.findById(id); // Obtener usuario por ID
        return expenseOptional.map(expenseMapper::toDTO);
    }

    @Override
    public Expense createExpense(ExpenseCreationDTO expenseCreationDTO) {
        // Validar y buscar la casa
        Optional<HouseDTO> houseOptional = houseService.getHouseById(expenseCreationDTO.getHouseId());

        if (houseOptional.isEmpty()) {
            throw new EntityNotFoundException("Casa no encontrada.");
        }

        House house = houseRepository.findById(houseOptional.get().getId())
                .orElseGet(() -> houseRepository.save(houseMapper.DTOToEntity(houseOptional.get())));

        // Crear gasto y asociar la casa
        Expense expense = expenseMapper.toEntity(expenseCreationDTO);
        expense.setHouse(house);

        // Guardar el gasto
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
