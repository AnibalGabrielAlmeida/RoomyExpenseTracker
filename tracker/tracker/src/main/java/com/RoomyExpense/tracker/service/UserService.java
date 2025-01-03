package com.RoomyExpense.tracker.service;

import com.RoomyExpense.tracker.DTO.*;
import com.RoomyExpense.tracker.mapper.HouseMapper;
import com.RoomyExpense.tracker.mapper.UserMapper;
import com.RoomyExpense.tracker.model.House;
import com.RoomyExpense.tracker.model.User;
import com.RoomyExpense.tracker.repository.HouseRepository;
import com.RoomyExpense.tracker.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import com.RoomyExpense.tracker.exception.HouseNotFoundException;
import com.RoomyExpense.tracker.exception.InvalidRoleException;
import com.RoomyExpense.tracker.exception.UserNotFoundException;

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
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));
        return Optional.of(userMapper.toUserDTO(user));
    }

    @Transactional
    @Override
    public UserDTO createUser(UserCreationDTO userCreationDTO) {
        System.out.println("Creating user with this information: " + userCreationDTO);

        User user = userMapper.toEntity(userCreationDTO);
        System.out.println("User mapped to entity: " + user);

        if (userCreationDTO.getHouseId() != null) {
            House house = houseRepository.findById(userCreationDTO.getHouseId())
                    .orElseThrow(() -> new HouseNotFoundException("House with ID " + userCreationDTO.getHouseId() + " not found"));
            user.setHouse(house);
        }

        User savedUser = userRepository.save(user);
        System.out.println("User saved in DB: " + savedUser);

        return userMapper.toUserDTO(savedUser);
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User with ID " + id + " not found");
        }
        userRepository.deleteById(id);
    }
    @Override
    @Transactional
    public User updateUser(Long userId, UserUpdateDTO userUpdateDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));

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
                    .orElseThrow(() -> new HouseNotFoundException("House with ID " + userUpdateDTO.getHouseId() + " not found"));
            user.setHouse(house);
        }

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public UserDTO changeUserRole(Long userId, UserRoleUpdateDTO updateRoleDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));

        if (updateRoleDTO.getRole() == null) {
            throw new InvalidRoleException("Role must not be null or empty");
        }

        user.setRole(updateRoleDTO.getRole());
        return userMapper.toUserDTO(userRepository.save(user));
    }

}
