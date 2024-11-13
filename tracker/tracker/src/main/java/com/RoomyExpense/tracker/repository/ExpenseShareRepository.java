package com.RoomyExpense.tracker.repository;

import com.RoomyExpense.tracker.model.ExpenseShare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseShareRepository extends JpaRepository<ExpenseShare, Long> {
}
