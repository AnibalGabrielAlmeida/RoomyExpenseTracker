package com.RoomyExpense.tracker.service;

import com.RoomyExpense.tracker.DTO.*;
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
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Validated
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private IHouseService houseService;

    @Autowired
    private HouseMapper houseMapper;

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll(); // Getting users List
        return users.stream()
                .map(userMapper::toUserDTO)//mapping user to dto
                .collect(Collectors.toList()); //to list
    }

    @Override
    public Optional<UserDTO> getUserById(Long id) {
        Optional<User> userOptional = userRepository.findById(id); // Getting user by id
        return userOptional.map(userMapper::toUserDTO); // mapping to user dto if it is present
    }

    @Transactional
    @Override
    public UserDTO createUser(UserCreationDTO userCreationDTO) {
        System.out.println("Creating user with this information: " + userCreationDTO);

        User user = userMapper.toEntity(userCreationDTO);
        System.out.println("User mapped to entity: " + user);

        if (userCreationDTO.getHouseId() != null) {
            Optional<House> existingHouse = houseRepository.findById(userCreationDTO.getHouseId());
            existingHouse.ifPresent(user::setHouse);
        }

        User savedUser = userRepository.save(user);
        System.out.println("User saved in DB: " + savedUser);

        return userMapper.toUserDTO(savedUser);
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {

        userRepository.deleteById(id);
    }

    @Transactional
    @Override
    public User updateUser(Long userId, UserUpdateDTO userUpdateDTO) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + userId + " not found"));

        if (userUpdateDTO.getName() != null) {
            user.setName(userUpdateDTO.getName());
        }
        if (userUpdateDTO.getEmail() != null) {
            user.setEmail(userUpdateDTO.getEmail());
        }
        if (userUpdateDTO.getPhoneNumber() != null) {
            user.setPhoneNumber(userUpdateDTO.getPhoneNumber());
        }
        if (userUpdateDTO.getHouseId() != null) {
            House house = houseRepository.findById(userUpdateDTO.getHouseId())
                    .orElseThrow(() -> new EntityNotFoundException("House with ID " + userUpdateDTO.getHouseId() + " not found"));
            user.setHouse(house);
        }

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public UserDTO changeUserRole(Long userId, UserRoleUpdateDTO updateRoleDTO) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + userId + " not found"));

        if (updateRoleDTO.getRole() == null) {
            throw new IllegalArgumentException("Role must not be null");
        }

        user.setRole(updateRoleDTO.getRole());
        user = userRepository.save(user);

        return userMapper.toUserDTO(user);
    }

}
