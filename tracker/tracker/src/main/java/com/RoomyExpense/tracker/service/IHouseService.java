package com.RoomyExpense.tracker.service;

import com.RoomyExpense.tracker.DTO.HouseCreationDTO;
import com.RoomyExpense.tracker.DTO.HouseDTO;
import com.RoomyExpense.tracker.DTO.HouseUpdateDTO;
import com.RoomyExpense.tracker.DTO.UserDTO;
import com.RoomyExpense.tracker.model.House;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public interface IHouseService {
    List<HouseDTO> getAllHouses();

    Optional<HouseDTO> getHouseById(Long id);

    @Transactional
    void deleteHouse(Long id);

    @Transactional
    HouseDTO createHouse(HouseCreationDTO houseCreationDTO);

    List<UserDTO> getRoommatesByHouseId(Long houseId);

    @Transactional
    HouseDTO addExistingUserToHouse(Long houseId, Long userId);

    @Transactional
    HouseDTO removeUserFromHouse(Long houseId, Long userId);

    @Transactional
    House updateHouse(Long houseId, HouseUpdateDTO houseUpdateDTO);
}
