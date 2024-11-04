package com.RoomyExpense.tracker.repository;

import com.RoomyExpense.tracker.model.House;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HouseRepository extends JpaRepository<House, Long> {
}
