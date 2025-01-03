package com.RoomyExpense.tracker.service;

import com.RoomyExpense.tracker.DTO.*;
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
        Optional<Payment> paymentOptional = paymentRepository.findById(id);
        return paymentOptional.map(paymentMapper::toDTO);
    }

    @Override
    public Payment createPayment(PaymentCreationDTO paymentCreationDTO) {
        try {
            log.info("Creating payment for user ID: " + paymentCreationDTO.getUserId());

            Optional<UserDTO> userOptional = userService.getUserById(paymentCreationDTO.getUserId());
            if (userOptional.isEmpty()) {
                throw new EntityNotFoundException("User not found");
            }

            log.info("User found: " + userOptional.get());

            Optional<ExpenseSplitDTO> expenseSplitOptional = expenseSplitService.getExpenseSplitById(paymentCreationDTO.getExpenseSplitId());
            if (expenseSplitOptional.isEmpty()) {
                throw new EntityNotFoundException("Expense not found");
            }

            log.info("ExpenseSplit found: " + expenseSplitOptional.get());

            User user = userRepository.findById(userOptional.get().getId())
                    .orElseThrow(() -> new EntityNotFoundException("User is not registered in the DB"));

            ExpenseSplit expenseSplit = expenseSplitRepository.findById(expenseSplitOptional.get().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Expense is not registered in the DB"));

            log.info("User: " + user + ", ExpenseSplit: " + expenseSplit);

            if (paymentCreationDTO.getAmount() == null || paymentCreationDTO.getAmount() <= 0) {
                throw new IllegalArgumentException("Payment amount must be a positive value.");
            }
            if (user == null || expenseSplit == null) {
                throw new EntityNotFoundException("User or ExpenseSplit could not be found.");
            }
            Double totalPagosRealizados = paymentRepository.findTotalPaymentsByExpenseSplit(expenseSplit.getId());
            if (totalPagosRealizados == null) {
                totalPagosRealizados = 0.0; // Asumir pagos realizados como 0 si no existen registros previos
            }
            log.info("Total payments made for ExpenseSplit " + expenseSplit.getId() + ": " + totalPagosRealizados);

            Double montoDisponible = expenseSplit.getAmount() - totalPagosRealizados;
            log.info("Remaining amount in ExpenseSplit " + expenseSplit.getId() + ": " + montoDisponible);

            if (paymentCreationDTO.getAmount() > montoDisponible) {
                throw new IllegalArgumentException("The payment amount (" + paymentCreationDTO.getAmount() +
                        ") exceeds the remaining amount (" + montoDisponible +
                        ") in the ExpenseSplit.");
            }

            Payment payment = paymentMapper.toEntity(paymentCreationDTO);
            payment.setUser(user);
            payment.setExpenseSplit(expenseSplit);

            Payment savedPayment = paymentRepository.save(payment);
            log.info("Payment created successfully: " + savedPayment);

            return savedPayment;
        } catch (Exception e) {
            log.severe("Error creating payment: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void deletePayment(Long id){
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
