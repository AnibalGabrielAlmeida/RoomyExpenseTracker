package com.RoomyExpense.tracker.service;


import com.RoomyExpense.tracker.model.Payment;
import com.RoomyExpense.tracker.model.User;

import java.util.List;
import java.util.Optional;

public interface IPaymentService {
    List<Payment> getAllPayments ();
    Optional<Payment> getPaymentById(Long id);
    void deletePayment(Long id);
    Payment savePayment(Payment payment);
}
