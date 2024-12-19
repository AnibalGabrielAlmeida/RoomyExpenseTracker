package com.RoomyExpense.tracker.service;

import com.RoomyExpense.tracker.DTO.HouseCreationDTO;
import com.RoomyExpense.tracker.DTO.HouseDTO;
import com.RoomyExpense.tracker.mapper.HouseMapper;
import com.RoomyExpense.tracker.model.House;
import com.RoomyExpense.tracker.model.User;
import com.RoomyExpense.tracker.repository.HouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class    HouseService implements IHouseService{


    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private HouseMapper houseMapper;


    @Override
    public List<HouseDTO> getAllHouses() {
        List<House> houses = houseRepository.findAll();
        return houses.stream()
                .map(houseMapper::toDTOWithRoommates)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<HouseDTO> getHouseById(Long id) {
        return houseRepository.findById(id)
                .map(houseMapper::toDTOWithRoommates);
    }



    @Override
    public HouseDTO createHouse(HouseCreationDTO houseCreationDTO) {
        House house = houseMapper.toEntity(houseCreationDTO);
        House savedHouse = houseRepository.save(house);
        return houseMapper.toDTO(savedHouse);
    }



    @Override
    public void deleteHouse(Long id) {
        houseRepository.deleteById(id);
    }

    @Override
    public List<User> getRoommatesByHouseId(Long houseId) {
        return houseRepository.findRoommatesByHouseId(houseId);
    }
}
