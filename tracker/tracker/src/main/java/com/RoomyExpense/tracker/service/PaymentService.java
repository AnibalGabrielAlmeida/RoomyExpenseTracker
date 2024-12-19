package com.RoomyExpense.tracker.service;

import com.RoomyExpense.tracker.DTO.ExpenseDTO;
import com.RoomyExpense.tracker.DTO.PaymentCreationDTO;
import com.RoomyExpense.tracker.DTO.PaymentDTO;
import com.RoomyExpense.tracker.DTO.UserDTO;
import com.RoomyExpense.tracker.mapper.ExpenseMapper;
import com.RoomyExpense.tracker.mapper.PaymentMapper;
import com.RoomyExpense.tracker.mapper.UserMapper;
import com.RoomyExpense.tracker.model.Expense;
import com.RoomyExpense.tracker.model.House;
import com.RoomyExpense.tracker.model.Payment;
import com.RoomyExpense.tracker.model.User;
import com.RoomyExpense.tracker.repository.ExpenseRepository;
import com.RoomyExpense.tracker.repository.PaymentRepository;
import com.RoomyExpense.tracker.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentService implements IPaymentService {

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ExpenseService expenseService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private PaymentMapper paymentMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ExpenseMapper expenseMapper;

    @Override
    public List<PaymentDTO> getAllPayments(){
        List<Payment> payments = paymentRepository.findAll();

        return payments.stream()
                .map(paymentMapper::toDTO)
                .collect(Collectors.toList());
    }
    //check optional handling
    public Optional<PaymentDTO> getPaymentById(Long id){
        Optional<Payment> paymentOptional = paymentRepository.findById(id);
        return paymentOptional.map(paymentMapper::toDTO);
    }

    @Override
    public Payment createPayment(PaymentCreationDTO paymentCreationDTO){
        Optional<UserDTO> userOptional = userService.getUserById(paymentCreationDTO.getUserId());
        Optional<ExpenseDTO> expenseOptional = expenseService.getExpenseById(paymentCreationDTO.getExpenseId());
        if(userOptional.isEmpty()){
            throw new EntityNotFoundException("Usuario no encontrado");
        }
        if(expenseOptional.isEmpty()){
            throw  new EntityNotFoundException("Gasto no encontrado");
        }
        User user = userRepository.findById(userOptional.get().getId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no existe en la base de datos"));

        Expense expense = expenseRepository.findById(expenseOptional.get().getId())
                .orElseThrow(() -> new EntityNotFoundException("Gasto no existe en la base de datos"));

        Payment payment = paymentMapper.toEntity(paymentCreationDTO);
        payment.setUser(user);
        payment.setExpense(expense);

        return paymentRepository.save(payment);
    }

    @Override
    public void deletePayment(Long id){
        paymentRepository.deleteById(id);
    }

}
