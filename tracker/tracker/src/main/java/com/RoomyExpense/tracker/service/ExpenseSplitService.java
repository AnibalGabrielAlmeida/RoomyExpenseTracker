package com.RoomyExpense.tracker.service;


import com.RoomyExpense.tracker.DTO.ExpenseDTO;
import com.RoomyExpense.tracker.DTO.ExpenseSplitCreationDTO;
import com.RoomyExpense.tracker.DTO.ExpenseSplitDTO;
import com.RoomyExpense.tracker.DTO.ExpenseSplitUpdateDTO;
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

    @Override
    public void deleteExpenseSplit(Long id) {
        expenseSplitRepository.deleteById(id);
    }

    @Transactional
    @Override
    public ExpenseSplit updateExpenseSplit(Long expenseSplitId, ExpenseSplitUpdateDTO expenseSplitUpdateDTO) {
        // Obtener el ExpenseSplit a actualizar
        ExpenseSplit expenseSplit = expenseSplitRepository.findById(expenseSplitId)
                .orElseThrow(() -> new EntityNotFoundException("ExpenseSplit with ID: " + expenseSplitId + " not found"));

        // Obtener todos los ExpenseSplits relacionados con el mismo Expense
        List<ExpenseSplit> allSplits = expenseSplitRepository.findByExpenseId(expenseSplit.getExpense().getId());

        // Validar que hay más de un split (no tiene sentido actualizar si solo hay uno)
        if (allSplits.size() <= 1) {
            throw new IllegalStateException("Cannot update a split when there is only one associated split.");
        }

        // Actualizar el monto o porcentaje del split
        if (expenseSplitUpdateDTO.getAmount() != null) {
            expenseSplit.setAmount(expenseSplitUpdateDTO.getAmount());
        }

        // Recalcular el resto de los splits
        double updatedAmount = expenseSplit.getAmount();
        double totalAmount = expenseSplit.getExpense().getAmount();

        // Validar que el nuevo monto no exceda el total
        if (updatedAmount > totalAmount) {
            throw new IllegalStateException("Updated amount exceeds the total expense amount.");
        }

        // Calcular cuánto queda para repartir entre los otros splits
        double remainingAmount = totalAmount - updatedAmount;

        // Actualizar los demás splits proporcionalmente
        List<ExpenseSplit> otherSplits = allSplits.stream()
                .filter(split -> !split.getId().equals(expenseSplitId))
                .collect(Collectors.toList());

        double totalOtherAmounts = otherSplits.stream().mapToDouble(ExpenseSplit::getAmount).sum();

        for (ExpenseSplit otherSplit : otherSplits) {
            double newAmount = (otherSplit.getAmount() / totalOtherAmounts) * remainingAmount;
            otherSplit.setAmount(newAmount);
        }

        // Guardar todos los cambios
        expenseSplitRepository.saveAll(allSplits);

        // Guardar y devolver el split actualizado
        return expenseSplitRepository.save(expenseSplit);
    }

    /*@Transactional
    @Override
    public ExpenseSplit updateExpenseSplit(Long expenseSplitId, ExpenseSplitUpdateDTO expenseSplitUpdateDTO) {
        ExpenseSplit expenseSplit = expenseSplitRepository.findById(expenseSplitId)
                .orElseThrow(() -> new EntityNotFoundException("Expense with ID: " + expenseSplitId + " not found"));
        if (expenseSplitUpdateDTO.getAmount() != null) {
            expenseSplit.setAmount(expenseSplitUpdateDTO.getAmount());
        }
        return expenseSplitRepository.save(expenseSplit);
    }*/

}
