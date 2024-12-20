package com.RoomyExpense.tracker.service;

import com.RoomyExpense.tracker.DTO.*;
import com.RoomyExpense.tracker.mapper.HouseMapper;
import com.RoomyExpense.tracker.mapper.UserMapper;
import com.RoomyExpense.tracker.model.House;
import com.RoomyExpense.tracker.model.User;
import com.RoomyExpense.tracker.repository.HouseRepository;
import com.RoomyExpense.tracker.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Validated
public class UserService implements  IUserService {

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

    @Override
    public UserDTO createUser(UserCreationDTO userCreationDTO) {
        System.out.println("Creating user with this information: " + userCreationDTO);

        //mapping to user
        User user = userMapper.toEntity(userCreationDTO);
        System.out.println("User mapped to entity: " + user);

        // Intentar asociar la casa
        Optional<HouseDTO> houseOptional = houseService.getHouseById(userCreationDTO.getHouseId());
        houseOptional.ifPresent(houseDTO -> {
            Optional<House> existingHouse = houseRepository.findById(houseDTO.getId());
            existingHouse.ifPresent(user::setHouse);
            System.out.println("house linked to user: " + existingHouse.orElse(null));
        });


        // Guardar usuario en la base de datos
        User savedUser = userRepository.save(user);
        System.out.println("User saved in DB: " + savedUser);

        return userMapper.toUserDTO(savedUser);
    }


    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }


    //todo transfer code to service, use mapper.
   @Override
    public UserDTO changeUserRole(Long userId, UserRoleUpdateDTO updateRoleDTO) {
        // User search
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new EntityNotFoundException("User not found");
        }

        User user = userOptional.get();



        user.setRole(updateRoleDTO.getRole());



        user = userRepository.save(user);


        UserDTO userDTO = new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRegistrationDate(),
                user.getHouse() != null ? user.getHouse().getName() : null,
                user.getHouse() != null ? "/api/houses/" + user.getHouse().getId() : null,
                user.getRole().toString()
        );

        return userDTO;
    }

}
