package com.RoomyExpense.tracker.controller;

import com.RoomyExpense.tracker.model.User;
import com.RoomyExpense.tracker.service.IUserService;
import com.RoomyExpense.tracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    IUserService userService;

    @GetMapping("/getAll")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/getById/{id}")
    public Optional<User> getUserByid(@RequestPart Long id){
        return userService.getUserById(id);
    }




}
