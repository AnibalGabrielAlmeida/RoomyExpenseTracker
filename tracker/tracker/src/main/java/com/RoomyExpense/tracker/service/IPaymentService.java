package com.RoomyExpense.tracker.service;


import com.RoomyExpense.tracker.DTO.PaymentCreationDTO;
import com.RoomyExpense.tracker.DTO.PaymentDTO;
import com.RoomyExpense.tracker.DTO.PaymentUpdateDTO;
import com.RoomyExpense.tracker.model.Payment;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public interface IPaymentService {
    List<PaymentDTO> getAllPayments();

    Optional<PaymentDTO> getPaymentById(Long id);

    @Transactional
    void deletePayment(Long id);

    @Transactional
    Payment createPayment(PaymentCreationDTO paymentCreationDTO);

    @Transactional
    Payment updatePayment(Long paymentId, PaymentUpdateDTO paymentUpdateDTO);
}
