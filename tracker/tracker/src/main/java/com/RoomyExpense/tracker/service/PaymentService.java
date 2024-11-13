package com.RoomyExpense.tracker.service;

import com.RoomyExpense.tracker.model.Payment;
import com.RoomyExpense.tracker.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService implements IPaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public List<Payment> getAllPayments(){
        return paymentRepository.findAll();
    }
    //check optional handling
    public Optional<Payment> getPaymentById(Long id){
        return paymentRepository.findById(id);
    }

    @Override
    public Payment savePayment(Payment payment){
        return paymentRepository.save(payment);
    }

    @Override
    public void deletePayment(Long id){
        paymentRepository.deleteById(id);
    }

}
