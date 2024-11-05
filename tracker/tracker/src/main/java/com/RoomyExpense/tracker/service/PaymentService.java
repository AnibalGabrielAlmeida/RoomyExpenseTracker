package com.RoomyExpense.tracker.service;

import com.RoomyExpense.tracker.model.Payment;
import com.RoomyExpense.tracker.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    public List<Payment> getAllPayments(){
        return paymentRepository.findAll();
    }

    public Payment savePayment(Payment payment){
        return paymentRepository.save(payment);
    }


}
