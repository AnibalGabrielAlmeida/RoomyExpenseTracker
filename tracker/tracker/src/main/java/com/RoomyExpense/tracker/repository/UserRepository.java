package com.RoomyExpense.tracker.repository;

import com.RoomyExpense.tracker.model.House;
import com.RoomyExpense.tracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
