package com.RoomyExpense.tracker.controller;

import com.RoomyExpense.tracker.DTO.PaymentCreationDTO;
import com.RoomyExpense.tracker.DTO.PaymentDTO;
import com.RoomyExpense.tracker.DTO.PaymentUpdateDTO;
import com.RoomyExpense.tracker.model.Payment;
import com.RoomyExpense.tracker.service.PaymentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/payments")
@Tag(name = "Payment Management", description = "Operations related to Payment management")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Operation(
        summary = "Create a new payment",
        description = "Creates a new payment with the provided information."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Payment created successfully",
            content = @Content(schema = @Schema(implementation = Payment.class))),
        @ApiResponse(responseCode = "404", description = "Related entity not found"),
        @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("/createPayment")
    public ResponseEntity<?> createPayment(
            @RequestBody(
                description = "Payment creation data",
                required = true,
                content = @Content(schema = @Schema(implementation = PaymentCreationDTO.class))
            )
            @org.springframework.web.bind.annotation.RequestBody PaymentCreationDTO paymentCreationDTO){
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

    @Operation(
        summary = "Get all payments",
        description = "Returns a list of all registered payments."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of payments or message if empty",
            content = @Content(schema = @Schema(implementation = PaymentDTO.class)))
    })
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllPayments() {
        List<PaymentDTO> paymentDTOs = paymentService.getAllPayments();
        if(paymentDTOs.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body("There are no payments currently registered.");
        }
        return ResponseEntity.ok(paymentDTOs);
    }

    @Operation(
        summary = "Get payment by ID",
        description = "Returns a payment by its ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Payment found",
            content = @Content(schema = @Schema(implementation = PaymentDTO.class))),
        @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getPaymentById(
            @Parameter(description = "ID of the payment to retrieve", required = true)
            @PathVariable Long id){
        Optional<PaymentDTO> paymentDTO = paymentService.getPaymentById(id);
        if(paymentDTO.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Payment not found");
        }
        return ResponseEntity.ok(paymentDTO.get());
    }

    @Operation(
        summary = "Delete payment by ID",
        description = "Deletes a payment by its ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Payment deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<?> deletePayment(
            @Parameter(description = "ID of the payment to delete", required = true)
            @PathVariable Long id){
        return paymentService.getPaymentById(id)
                .map(paymentDTO -> {
                    paymentService.deletePayment(id);
                    return ResponseEntity.status(HttpStatus.OK).body("Payment with Id: " + id + " Succesfully eliminated");
                }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Payment with Id: " + id + " not found"));
    }

    @Operation(
        summary = "Update payment",
        description = "Updates payment information by ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Payment updated successfully",
            content = @Content(schema = @Schema(implementation = Payment.class))),
        @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    @PatchMapping("/updatePayment/{paymentId}")
    public ResponseEntity<Payment> updatePayment(
            @Parameter(description = "ID of the payment to update", required = true)
            @PathVariable Long paymentId,
            @RequestBody(
                description = "Payment update data",
                required = true,
                content = @Content(schema = @Schema(implementation = PaymentUpdateDTO.class))
            )
            @org.springframework.web.bind.annotation.RequestBody PaymentUpdateDTO paymentUpdateDTO) {

        Payment updatedPayment = paymentService.updatePayment(paymentId, paymentUpdateDTO);

        return ResponseEntity.ok(updatedPayment);
    }
}
