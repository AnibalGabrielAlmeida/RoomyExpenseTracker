package com.RoomyExpense.tracker.service;

import com.RoomyExpense.tracker.model.House;
import com.RoomyExpense.tracker.model.User;

import java.util.List;
import java.util.Optional;

public interface IHouseService {
    List<House> getAllHouses ();
    Optional<House> getHouseById(Long id);
    void deleteHouse(Long id);
    House saveHouse(House house);
    List<User> getRoommatesByHouseId(Long houseId);
}
