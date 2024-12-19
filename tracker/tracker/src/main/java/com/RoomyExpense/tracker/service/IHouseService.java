package com.RoomyExpense.tracker.service;

import com.RoomyExpense.tracker.DTO.HouseCreationDTO;
import com.RoomyExpense.tracker.DTO.HouseDTO;
import com.RoomyExpense.tracker.DTO.UserDTO;
import com.RoomyExpense.tracker.model.House;
import com.RoomyExpense.tracker.model.User;

import java.util.List;
import java.util.Optional;

public interface IHouseService {
    List<HouseDTO> getAllHouses ();
    Optional<HouseDTO> getHouseById(Long id);
    void deleteHouse(Long id);
    HouseDTO createHouse(HouseCreationDTO houseCreationDTO);
    List<UserDTO> getRoommatesByHouseId(Long houseId);
}
