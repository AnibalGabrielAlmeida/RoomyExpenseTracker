package com.RoomyExpense.tracker.controller;

import com.RoomyExpense.tracker.DTO.PaymentCreationDTO;
import com.RoomyExpense.tracker.DTO.PaymentDTO;
import com.RoomyExpense.tracker.model.Expense;
import com.RoomyExpense.tracker.model.Payment;
import com.RoomyExpense.tracker.model.User;
import com.RoomyExpense.tracker.service.ExpenseService;
import com.RoomyExpense.tracker.service.PaymentService;
import com.RoomyExpense.tracker.service.UserService;
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
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserService userService;

    @Autowired
    private ExpenseService expenseService;

    // Obtener todos los pagos (historial de pagos)
    @GetMapping
    public ResponseEntity<List<PaymentDTO>> getAllPayments() {
        List<PaymentDTO> paymentDTOs = paymentService.getAllPayments().stream()
                .map(payment -> new PaymentDTO(
                        payment.getId(),
                        payment.getPaymentDate(),
                        payment.getAmount(),
                        payment.getState().toString(),
                        payment.getUser().getName(),
                        payment.getExpense().getName()
                ))
                .collect(Collectors.toList());

        return new ResponseEntity<>(paymentDTOs, HttpStatus.OK);
    }

    // Crear un pago (básico, solo con monto, usuario y gasto)
    @PostMapping
    public ResponseEntity<PaymentDTO> createPayment(@Validated @RequestBody PaymentCreationDTO paymentCreationDTO,
                                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Buscar el usuario y gasto en la base de datos
        Optional<User> user = userService.getUserById(paymentCreationDTO.getUserId());
        Optional<Expense> expense = expenseService.getExpenseById(paymentCreationDTO.getExpenseId());

        if (user.isEmpty() || expense.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Payment payment = new Payment();
        payment.setPaymentDate(java.time.LocalDate.now()); // Asignamos la fecha actual
        payment.setAmount(paymentCreationDTO.getAmount());
        payment.setUser(user.get()); // Asignamos el usuario encontrado
        payment.setExpense(expense.get()); // Asignamos el gasto encontrado
        payment.setState(Payment.State.UNPAID); // Por defecto se pone como UNPAID

        Payment savedPayment = paymentService.savePayment(payment);

        PaymentDTO paymentDTO = new PaymentDTO(
                savedPayment.getId(),
                savedPayment.getPaymentDate(),
                savedPayment.getAmount(),
                savedPayment.getState().toString(),
                savedPayment.getUser().getName(),
                savedPayment.getExpense().getName()
        );

        return new ResponseEntity<>(paymentDTO, HttpStatus.CREATED);
    }

    // Eliminar un pago por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        Optional<Payment> payment = paymentService.getPaymentById(id);
        if (payment.isPresent()) {
            paymentService.deletePayment(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Obtener el total de deudas (pagos no realizados)
    @GetMapping("/deudas")
    public ResponseEntity<List<PaymentDTO>> getDebtSummary() {
        List<PaymentDTO> debtSummary = paymentService.getAllPayments().stream()
                .filter(payment -> payment.getState() == Payment.State.UNPAID)
                .map(payment -> new PaymentDTO(
                        payment.getId(),
                        payment.getPaymentDate(),
                        payment.getAmount(),
                        payment.getState().toString(),
                        payment.getUser().getName(),
                        payment.getExpense().getName()
                ))
                .collect(Collectors.toList());

        return new ResponseEntity<>(debtSummary, HttpStatus.OK);
    }
}
