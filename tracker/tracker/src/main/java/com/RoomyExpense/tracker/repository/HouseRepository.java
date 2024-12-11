package com.RoomyExpense.tracker.repository;

import com.RoomyExpense.tracker.model.House;
import com.RoomyExpense.tracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HouseRepository extends JpaRepository<House, Long> {
    @Query("SELECT h.roommates FROM House h WHERE h.id = :houseId")
    List<User> findRoommatesByHouseId(@Param("houseId") Long houseId);
}
