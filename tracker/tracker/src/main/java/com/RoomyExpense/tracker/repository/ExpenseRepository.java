package com.RoomyExpense.tracker.repository;

import com.RoomyExpense.tracker.model.Expense;
import com.RoomyExpense.tracker.model.House;
import com.RoomyExpense.tracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByHouse(House house);
}
