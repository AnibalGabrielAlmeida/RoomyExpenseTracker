package com.RoomyExpense.tracker.service;

import com.RoomyExpense.tracker.DTO.UserDTO;
import com.RoomyExpense.tracker.DTO.UserRoleUpdateDTO;
import com.RoomyExpense.tracker.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<User> getAllUsers ();
    Optional<User> getUserById(Long id);
    void deleteUser(Long id);
    User saveUser(User user);

    UserDTO changeUserRole(Long userId, UserRoleUpdateDTO updateRoleDTO);
}
