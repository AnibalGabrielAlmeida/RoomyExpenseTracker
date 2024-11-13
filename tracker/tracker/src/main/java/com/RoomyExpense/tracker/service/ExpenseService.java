package com.RoomyExpense.tracker.service;

import com.RoomyExpense.tracker.DTO.ExpenseDTO;
import com.RoomyExpense.tracker.model.Expense;
import com.RoomyExpense.tracker.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ExpenseService {

  /*  @Autowired
    private ExpenseRepository expenseRepository;

    public List<Expense> getAllExpenses(){
        return expenseRepository.findAll();
    }

    public Expense saveExpense(Expense expense){
        return expenseRepository.save(expense);
    }

    public void deleteExpense(Long id){
        expenseRepository.deleteById(id);
    }

    public ExpenseDTO updateExpense(Long expenseId, ExpenseDTO updatedExpenseDTO) {
        // Buscar el gasto existente
        Expense existingExpense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new NoSuchElementException("Expense with ID " + expenseId + " not found"));

        // Actualizar detalles
        existingExpense.setName(updatedExpenseDTO.getName());
        existingExpense.setCategory(updatedExpenseDTO.getCategory());
        existingExpense.setAmount(updatedExpenseDTO.getAmount());

        // Guardar y devolver la entidad actualizada
        Expense updatedExpense = expenseRepository.save(existingExpense);
        return convertToDTO(updatedExpense);
    }

    public PaymentDTO registerUserPayment(Long expenseId, Long userId, double amountPaid) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new NoSuchElementException("Expense with ID " + expenseId + " not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with ID " + userId + " not found"));

        // Verificar si el usuario ya ha pagado y actualizar su estado de pago
        Payment payment = new Payment(expense, user, amountPaid);
        paymentRepository.save(payment);

        return convertPaymentToDTO(payment);
    }

    public List<PaymentStatusDTO> getPaymentStatus(Long expenseId) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new NoSuchElementException("Expense with ID " + expenseId + " not found"));

        List<Payment> payments = paymentRepository.findByExpense(expense);
        return payments.stream()
                .map(this::convertPaymentToStatusDTO)
                .collect(Collectors.toList());
    }
*/
}
