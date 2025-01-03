package com.RoomyExpense.tracker.controller;

import com.RoomyExpense.tracker.DTO.PaymentCreationDTO;
import com.RoomyExpense.tracker.DTO.PaymentDTO;
import com.RoomyExpense.tracker.DTO.PaymentUpdateDTO;
import com.RoomyExpense.tracker.model.Payment;
import com.RoomyExpense.tracker.service.ExpenseService;
import com.RoomyExpense.tracker.service.PaymentService;
import com.RoomyExpense.tracker.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/createPayment")
    public ResponseEntity<?> createPayment(@RequestBody PaymentCreationDTO paymentCreationDTO){
        try {
            Payment payment = paymentService.createPayment(paymentCreationDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Payment successfully created ID: " + payment.getId() +
                            "\n User: " + payment.getUser().getName()
                            + "\nExpense: " + payment.getExpenseSplit().getExpense().getName());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while creating the payment.");
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllPayments() {
        List<PaymentDTO> paymentDTOs = paymentService.getAllPayments();
        if(paymentDTOs.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body("There are no payments currently registered.");
        }
        return ResponseEntity.ok(paymentDTOs);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getPaymentById(@PathVariable Long id){
        Optional<PaymentDTO> paymentDTO = paymentService.getPaymentById(id);
        if(paymentDTO.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body("Payment not found");
        }
        return ResponseEntity.ok(paymentDTO.get());
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<?> deletePayment(@PathVariable Long id){
        return paymentService.getPaymentById(id)
                .map(paymentDTO -> {
                    paymentService.deletePayment(id);
                    return ResponseEntity.status(HttpStatus.OK).body("Payment with Id: " + id + " Succesfully eliminated");
                }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Payment with Id: " + id + " not found"));
    }

    @PatchMapping("/updatePayment/{paymentId}")
    public ResponseEntity<Payment> updatePayment(
            @PathVariable Long paymentId,
            @RequestBody PaymentUpdateDTO paymentUpdateDTO) {

        Payment updatedPayment = paymentService.updatePayment(paymentId, paymentUpdateDTO);

        return ResponseEntity.ok(updatedPayment);
    }
}
