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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

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
        List<User> users = userRepository.findAll(); // Obtener lista de usuarios
        return users.stream()
                .map(userMapper::toUserDTO)
                .collect(Collectors.toList()); // Convertir a UserDTO
    }

    @Override
    public Optional<UserDTO> getUserById(Long id) {
        Optional<User> userOptional = userRepository.findById(id); // Obtener usuario por ID
        return userOptional.map(userMapper::toUserDTO); // Convertir a UserDTO si está presente
    }

    @Override
    public UserDTO createUser(UserCreationDTO userCreationDTO) {
        System.out.println("Creando usuario con datos: " + userCreationDTO);

        // Mapeo de usuario
        User user = userMapper.toEntity(userCreationDTO);
        System.out.println("Usuario convertido a entidad: " + user);

        // Intentar asociar la casa
        Optional<HouseDTO> houseOptional = houseService.getHouseById(userCreationDTO.getHouseId());
        houseOptional.ifPresent(houseDTO -> {
            Optional<House> existingHouse = houseRepository.findById(houseDTO.getId());
            existingHouse.ifPresent(user::setHouse);
            System.out.println("Casa asociada al usuario: " + existingHouse.orElse(null));
        });


        // Guardar usuario en la base de datos
        User savedUser = userRepository.save(user);
        System.out.println("Usuario guardado en base de datos: " + savedUser);

        return userMapper.toUserDTO(savedUser);
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
