package com.RoomyExpense.tracker.service;

import com.RoomyExpense.tracker.DTO.UserDTO;
import com.RoomyExpense.tracker.DTO.UserRoleUpdateDTO;
import com.RoomyExpense.tracker.model.User;
import com.RoomyExpense.tracker.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.*;

@Service
@Validated
public class UserService implements  IUserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }
    //check optional handling

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

   @Override
    public UserDTO changeUserRole(Long userId, UserRoleUpdateDTO updateRoleDTO) {
        // Buscar usuario
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new EntityNotFoundException("Usuario no encontrado");
        }

        User user = userOptional.get();

        // Mapeo desde UserUpdateDTO a User

        user.setRole(updateRoleDTO.getRole());


        // Guardar usuario actualizado
        user = userRepository.save(user);

        // Mapeo desde User a UserDTO para mostrar los datos actualizados
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
