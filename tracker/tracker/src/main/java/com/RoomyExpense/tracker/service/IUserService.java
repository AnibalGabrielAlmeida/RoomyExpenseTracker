package com.RoomyExpense.tracker.service;

import com.RoomyExpense.tracker.DTO.UserCreationDTO;
import com.RoomyExpense.tracker.DTO.UserDTO;
import com.RoomyExpense.tracker.DTO.UserRoleUpdateDTO;
import com.RoomyExpense.tracker.DTO.UserUpdateDTO;
import com.RoomyExpense.tracker.model.User;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<UserDTO> getAllUsers();

    Optional<UserDTO> getUserById(Long id);

    void deleteUser(Long id);

    UserDTO createUser(UserCreationDTO userCreationDTO);

    User updateUser(Long userId, UserUpdateDTO userUpdateDTO);

    UserDTO changeUserRole(Long userId, UserRoleUpdateDTO updateRoleDTO);
}
