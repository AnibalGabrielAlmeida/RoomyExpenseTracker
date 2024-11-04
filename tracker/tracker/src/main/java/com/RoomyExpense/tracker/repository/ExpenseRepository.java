package com.RoomyExpense.tracker.repository;

import com.RoomyExpense.tracker.model.Expense;
import com.RoomyExpense.tracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}
