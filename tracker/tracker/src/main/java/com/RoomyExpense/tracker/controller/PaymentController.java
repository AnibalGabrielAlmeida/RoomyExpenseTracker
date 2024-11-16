package com.RoomyExpense.tracker.controller;

import com.RoomyExpense.tracker.model.Expense;
import com.RoomyExpense.tracker.model.Payment;
import com.RoomyExpense.tracker.service.IExpenseService;
import com.RoomyExpense.tracker.service.IPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    IPaymentService paymentService;

    //add Response Entity to controllers
    @GetMapping("/getAll")
    public List<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/getById/{id}")
    public Optional<Payment> getPaymentByid(@PathVariable Long id) {
        return paymentService.getPaymentById(id);
    }

    @PostMapping("/savePayment")
    public Payment savePayment(@RequestBody Payment payment) {
        return paymentService.savePayment(payment);
    }

    @DeleteMapping("/deleteById/{id}")
    public void deletePaymentById(@PathVariable Long id) {
        paymentService.deletePayment(id);
    }
}
