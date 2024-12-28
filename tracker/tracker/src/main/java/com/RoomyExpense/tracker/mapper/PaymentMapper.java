package com.RoomyExpense.tracker.mapper;

import com.RoomyExpense.tracker.DTO.PaymentCreationDTO;
import com.RoomyExpense.tracker.DTO.PaymentDTO;
import com.RoomyExpense.tracker.model.Payment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Locale;

@Component
public class PaymentMapper {
    public Payment toEntity(PaymentCreationDTO paymentCreationDTO){
        Payment payment = new Payment();

        payment.setAmount(paymentCreationDTO.getAmount());
        payment.setPaymentDate(LocalDateTime.now().toLocalDate());
        //expense y user manejado en la logica de servicio.

        return payment;
    }

    public PaymentDTO toDTO (Payment payment){
        return new PaymentDTO(
                payment.getPaymentDate(),
                payment.getAmount(),
                payment.getUser().getName(),
                payment.getExpense().getName()
        );
    }
}
