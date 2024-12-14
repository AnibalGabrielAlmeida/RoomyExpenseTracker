package com.RoomyExpense.tracker.mapper;

import com.RoomyExpense.tracker.DTO.UserCreationDTO;
import com.RoomyExpense.tracker.DTO.UserDTO;
import com.RoomyExpense.tracker.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

import java.util.stream.Collectors;

@Component
public class UserMapper {


    public UserDTO toUserDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRegistrationDate(),
                user.getHouse() != null ? user.getHouse().getName() : null,
                user.getHouse() != null ? "/api/houses/" + user.getHouse().getId() : null,
                user.getRole().name()
        );
    }

    public User toEntity(UserCreationDTO userCreationDTO){
        User user = new User();

        user.setName(userCreationDTO.getName());
        user.setEmail(userCreationDTO.getEmail());
        //todo finish encoding
        //user.setPassword(passwordEncoder.encode(userCreationDTO.getPassword()));
        user.setPassword(userCreationDTO.getPassword());
        user.setPassword((userCreationDTO.getPassword()));
        user.setPhoneNumber(userCreationDTO.getPhoneNumber());
        user.setRole(User.UserRole.valueOf(userCreationDTO.getRole().toUpperCase()));

        user.setRegistrationDate(LocalDate.now());
        return user;
    }


    public List<UserDTO> toUserDTOs(List<User> users) {
        return users.stream()
                .map(this::toUserDTO)
                .collect(Collectors.toList());
    }

}
