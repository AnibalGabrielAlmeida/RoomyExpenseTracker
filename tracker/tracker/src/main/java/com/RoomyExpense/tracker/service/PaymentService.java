package com.RoomyExpense.tracker.service;

import com.RoomyExpense.tracker.DTO.*;
import com.RoomyExpense.tracker.exception.ExpenseNotFoundException;
import com.RoomyExpense.tracker.exception.PaymentNotFoundException;
import com.RoomyExpense.tracker.exception.UserNotFoundException;
import com.RoomyExpense.tracker.mapper.PaymentMapper;
import com.RoomyExpense.tracker.model.ExpenseSplit;
import com.RoomyExpense.tracker.model.Payment;
import com.RoomyExpense.tracker.model.User;
import com.RoomyExpense.tracker.repository.ExpenseSplitRepository;
import com.RoomyExpense.tracker.repository.PaymentRepository;
import com.RoomyExpense.tracker.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class PaymentService implements IPaymentService {

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ExpenseSplitService expenseSplitService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ExpenseSplitRepository expenseSplitRepository;
    @Autowired
    private PaymentMapper paymentMapper;

    private static final Logger log = Logger.getLogger(PaymentService.class.getName());


    @Override
    public List<PaymentDTO> getAllPayments(){
        List<Payment> payments = paymentRepository.findAll();

        return payments.stream()
                .map(paymentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PaymentDTO> getPaymentById(Long id){
        Payment payment  = paymentRepository.findById(id)
                .orElseThrow(()-> new PaymentNotFoundException("Payment with ID " + id + " not found"));
        return Optional.of(paymentMapper.toDTO(payment));
    }

    @Transactional
    @Override
    public Payment createPayment(PaymentCreationDTO paymentCreationDTO) {
        try {
            // Retrieve user based on ID
            UserDTO userDTO = userService.getUserById(paymentCreationDTO.getUserId())
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            // Retrieve ExpenseSplit based on ID
            ExpenseSplitDTO expenseSplitDTO = expenseSplitService.getExpenseSplitById(paymentCreationDTO.getExpenseSplitId())
                    .orElseThrow(() -> new ExpenseNotFoundException("Expense not found"));

            User user = userRepository.findById(userDTO.getId())
                    .orElseThrow(() -> new UserNotFoundException("User is not registered in the DB"));

            ExpenseSplit expenseSplit = expenseSplitRepository.findById(expenseSplitDTO.getId())
                    .orElseThrow(() -> new ExpenseNotFoundException("Expense is not registered in the DB"));

            // Validate payment amount
            if (paymentCreationDTO.getAmount() == null || paymentCreationDTO.getAmount() <= 0) {
                throw new IllegalArgumentException("Payment amount must be a positive value.");
            }

            Double totalPaymentsMade = paymentRepository.findTotalPaymentsByExpenseSplit(expenseSplit.getId());
            if (totalPaymentsMade == null) {
                totalPaymentsMade = 0.0; // Assume no previous payments if none exist
            }

            Double availableAmount = expenseSplit.getAmount() - totalPaymentsMade;

            // Check if the payment amount is valid
            if (paymentCreationDTO.getAmount() > availableAmount) {
                throw new IllegalArgumentException("The payment amount exceeds the remaining amount in the ExpenseSplit.");
            }

            Payment payment = paymentMapper.toEntity(paymentCreationDTO);
            payment.setUser(user);
            payment.setExpenseSplit(expenseSplit);

            return paymentRepository.save(payment);
        } catch (Exception e) {
            log.severe("Error creating payment: " + e.getMessage());
            throw e;
        }
    }



    @Transactional
    @Override
    public void deletePayment(Long id){

        if (!paymentRepository.existsById(id)) {
            throw new UserNotFoundException("Payment with ID " + id + " not found");
        }
        paymentRepository.deleteById(id);
    }


    @Transactional
    @Override
    public Payment updatePayment(Long paymentId, PaymentUpdateDTO paymentUpdateDTO) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment with ID " + paymentId + " not found"));

        if (paymentUpdateDTO.getAmount() != null) {
            payment.setAmount(paymentUpdateDTO.getAmount());
        }
        return paymentRepository.save(payment);
    }
}
