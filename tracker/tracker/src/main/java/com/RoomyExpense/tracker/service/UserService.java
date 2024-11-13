package com.RoomyExpense.tracker.service;

import com.RoomyExpense.tracker.DTO.UserUpdateDTO;
import com.RoomyExpense.tracker.model.User;
import com.RoomyExpense.tracker.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.beans.PropertyDescriptor;
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
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

   /*think the update method
   public User updateUser(Long id, @Valid UserUpdateDTO userUpdateDTO) {
       // Buscar el usuario existente
       User existingUser = userRepository.findById(id)
               .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

       // Actualizar solo las propiedades no nulas
       if (userUpdateDTO.getName() != null) {
           existingUser.setName(userUpdateDTO.getName());
       }
       if (userUpdateDTO.getEmail() != null) {
           existingUser.setEmail(userUpdateDTO.getEmail());
       }


       // Guardar cambios en la base de datos
       return userRepository.save(existingUser);
   }*/
}
