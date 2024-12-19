package com.RoomyExpense.tracker.controller;

import com.RoomyExpense.tracker.DTO.PaymentCreationDTO;
import com.RoomyExpense.tracker.DTO.PaymentDTO;
import com.RoomyExpense.tracker.model.Expense;
import com.RoomyExpense.tracker.model.Payment;
import com.RoomyExpense.tracker.model.User;
import com.RoomyExpense.tracker.service.ExpenseService;
import com.RoomyExpense.tracker.service.PaymentService;
import com.RoomyExpense.tracker.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserService userService;

    @Autowired
    private ExpenseService expenseService;

    @PostMapping("/createPayment")
    public ResponseEntity<?> createPayment(@RequestBody PaymentCreationDTO paymentCreationDTO){
        try {
            Payment payment = paymentService.createPayment(paymentCreationDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Pago creado exitosamente. Usuario: " + payment.getUser().getName()
                            + "\nGasto: " + payment.getExpense().getName() );
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear el pago.");
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllPayments() {
        List<PaymentDTO> paymentDTOs = paymentService.getAllPayments();
        if(paymentDTOs.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body("No hay pagos registrados actualmente.");
        }
        return ResponseEntity.ok(paymentDTOs);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getPaymentById(@PathVariable Long id){
        Optional<PaymentDTO> paymentDTO = paymentService.getPaymentById(id);
        if(paymentDTO.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body("Pago no encontrado");
        }
        return ResponseEntity.ok(paymentDTO.get());
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<?> deletePayment(@PathVariable Long id){
        return paymentService.getPaymentById(id)
                .map(paymentDTO -> {
                    paymentService.deletePayment(id);
                    return ResponseEntity.status(HttpStatus.OK).body("Pago con Id: " + id + " eliminado exitosamente");
                }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pago con Id: " + id + " no encontrado"));
    }

}
