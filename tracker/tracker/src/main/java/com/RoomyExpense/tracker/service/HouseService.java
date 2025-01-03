package com.RoomyExpense.tracker.service;

import com.RoomyExpense.tracker.DTO.HouseCreationDTO;
import com.RoomyExpense.tracker.DTO.HouseDTO;
import com.RoomyExpense.tracker.DTO.HouseUpdateDTO;
import com.RoomyExpense.tracker.DTO.UserDTO;
import com.RoomyExpense.tracker.mapper.HouseMapper;
import com.RoomyExpense.tracker.mapper.UserMapper;
import com.RoomyExpense.tracker.model.House;
import com.RoomyExpense.tracker.model.User;
import com.RoomyExpense.tracker.repository.HouseRepository;
import com.RoomyExpense.tracker.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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
    private UserRepository userRepository;
    @Autowired
    private HouseMapper houseMapper;
    @Autowired
    private UserMapper userMapper;

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

    @Transactional
    @Override
    public HouseDTO createHouse(HouseCreationDTO houseCreationDTO) {
        House house = houseMapper.toEntity(houseCreationDTO);
        House savedHouse = houseRepository.save(house);
        return houseMapper.toDTO(savedHouse);
    }

    @Transactional
    @Override
    public void deleteHouse(Long id) {

        houseRepository.deleteById(id);
    }

    @Override
    public List<UserDTO> getRoommatesByHouseId(Long houseId) {
        List<User> roommates = houseRepository.findRoommatesByHouseId(houseId);
        return   roommates.stream()
                .map(userMapper::toUserDTO)
                .collect(Collectors.toList());


    }

    @Transactional
    @Override
    public HouseDTO addExistingUserToHouse(Long houseId, Long userId) {
        // Get the house by ID
        Optional<House> houseOptional = houseRepository.findById(houseId);
        if (houseOptional.isEmpty()) {
            throw new IllegalArgumentException("Casa con ID " + houseId + " no encontrada.");
        }
        House house = houseOptional.get();

        // Get user by ID
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("Usuario con ID " + userId + " no encontrado.");
        }
        User user = userOptional.get();

        // Check if the user is already assigned to a house
        if (user.getHouse() != null) {
            throw new IllegalStateException("El usuario ya está asignado a una casa.");
        }

        // Establish the bidirectional relationship
        user.setHouse(house);
        house.getRoommates().add(user);

        // Save changes
        userRepository.save(user);
        houseRepository.save(house);

        // Create the updated DTO
        return houseMapper.toDTO(house);
    }

    @Transactional
    @Override
    public HouseDTO removeUserFromHouse(Long houseId, Long userId){
        Optional<House> houseOptional = houseRepository.findById(houseId);

        if (houseOptional.isEmpty()) {
            throw new  IllegalArgumentException("Usuario con ID " + userId + " no encontrado.");
        }

        House house = houseOptional.get();

        // Search user in the roommates list
        Optional<User> userOptional = house.getRoommates().stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst();

        if (userOptional.isEmpty()) {
            throw new IllegalStateException("No hay usuario con ese ID asignado a la casa.");
        }

        User user = userOptional.get();

        // delete the user from the roommates list
        house.getRoommates().remove(user);

        // unbind house from user
        user.setHouse(null);

        // save changes
        houseRepository.save(house);
        userRepository.save(user);

        return houseMapper.toDTO(house);
    }

    @Override
    @Transactional
    public House updateHouse(Long houseId, HouseUpdateDTO houseUpdateDTO) {
        House house = houseRepository.findById(houseId)
                .orElseThrow(() -> new EntityNotFoundException("House with ID " + houseId + " not found"));

        if (houseUpdateDTO.getName() != null) {
            house.setName(houseUpdateDTO.getName());
        }
        if (houseUpdateDTO.getAddress() != null) {
            house.setAddress(houseUpdateDTO.getAddress());
        }

        return houseRepository.save(house);
    }

}
