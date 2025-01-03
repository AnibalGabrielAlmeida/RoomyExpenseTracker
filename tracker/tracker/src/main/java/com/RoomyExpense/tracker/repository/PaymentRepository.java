package com.RoomyExpense.tracker.repository;

import com.RoomyExpense.tracker.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.expenseSplit.id = :expenseSplitId")
    Double findTotalPaymentsByExpenseSplit(@Param("expenseSplitId") Long expenseSplitId);
}
