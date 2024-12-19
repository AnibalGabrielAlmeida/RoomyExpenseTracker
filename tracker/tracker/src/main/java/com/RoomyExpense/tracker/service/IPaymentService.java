package com.RoomyExpense.tracker.service;


import com.RoomyExpense.tracker.DTO.PaymentCreationDTO;
import com.RoomyExpense.tracker.DTO.PaymentDTO;
import com.RoomyExpense.tracker.model.Payment;
import com.RoomyExpense.tracker.model.User;

import java.util.List;
import java.util.Optional;

public interface IPaymentService {
    List<PaymentDTO> getAllPayments ();
    Optional<PaymentDTO> getPaymentById(Long id);
    void deletePayment(Long id);
    Payment createPayment(PaymentCreationDTO paymentCreationDTO);
}
