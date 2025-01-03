package com.RoomyExpense.tracker.repository;


import com.RoomyExpense.tracker.model.ExpenseSplit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseSplitRepository extends JpaRepository<ExpenseSplit, Long> {
    <S extends ExpenseSplit> List<S> saveAll(Iterable<S> entities);

    List<ExpenseSplit> findByExpenseId(Long expenseId);
}
