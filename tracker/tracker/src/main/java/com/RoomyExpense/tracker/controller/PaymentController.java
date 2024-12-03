package com.RoomyExpense.tracker.controller;

import com.RoomyExpense.tracker.model.Payment;
import com.RoomyExpense.tracker.service.IPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private IPaymentService paymentService;

    @GetMapping("/getAll")
    public ResponseEntity<List<Payment>> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        if (payments.isEmpty()) {
            return ResponseEntity.status(404).body(null); // Puedes manejar el caso de lista vacía si lo necesitas
        }
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
        Optional<Payment> paymentOptional = paymentService.getPaymentById(id);
        if (paymentOptional.isEmpty()) {
            return ResponseEntity.status(404).body(null);
        }
        return ResponseEntity.ok(paymentOptional.get());
    }

    @PostMapping("/savePayment")
    public ResponseEntity<Payment> savePayment(@RequestBody Payment payment) {
        Payment savedPayment = paymentService.savePayment(payment);
        return ResponseEntity.status(201).body(savedPayment); // 201: Created
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<String> deletePaymentById(@PathVariable Long id) {
        Optional<Payment> paymentOptional = paymentService.getPaymentById(id);
        if (paymentOptional.isEmpty()) {
            return ResponseEntity.status(404).body("Payment with ID " + id + " not found");
        }
        paymentService.deletePayment(id);
        return ResponseEntity.ok("Payment with ID " + id + " deleted successfully");
    }
}
