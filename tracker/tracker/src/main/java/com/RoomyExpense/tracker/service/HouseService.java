package com.RoomyExpense.tracker.service;

import com.RoomyExpense.tracker.DTO.HouseCreationDTO;
import com.RoomyExpense.tracker.DTO.HouseDTO;
import com.RoomyExpense.tracker.DTO.UserDTO;
import com.RoomyExpense.tracker.mapper.HouseMapper;
import com.RoomyExpense.tracker.mapper.UserMapper;
import com.RoomyExpense.tracker.model.House;
import com.RoomyExpense.tracker.model.User;
import com.RoomyExpense.tracker.repository.HouseRepository;
import com.RoomyExpense.tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public List<UserDTO> getRoommatesByHouseId(Long houseId) {
        List<User> roommates = houseRepository.findRoommatesByHouseId(houseId);
        return   roommates.stream()
                .map(userMapper::toUserDTO)
                .collect(Collectors.toList());


    }

    @Override
    public HouseDTO addExistingUserToHouse(Long houseId, Long userId) {
        // Obtener la casa por ID
        Optional<House> houseOptional = houseRepository.findById(houseId);
        if (houseOptional.isEmpty()) {
            throw new IllegalArgumentException("Casa con ID " + houseId + " no encontrada.");
        }
        House house = houseOptional.get();

        // Obtener el usuario por ID
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("Usuario con ID " + userId + " no encontrado.");
        }
        User user = userOptional.get();

        // Verificar si el usuario ya está asignado a una casa
        if (user.getHouse() != null) {
            throw new IllegalStateException("El usuario ya está asignado a una casa.");
        }

        // Establecer la relación bidireccional
        user.setHouse(house);
        house.getRoommates().add(user);

        // Guardar los cambios
        userRepository.save(user);
        houseRepository.save(house);

        // Crear el DTO actualizado
        return houseMapper.toDTO(house);
    }



}
