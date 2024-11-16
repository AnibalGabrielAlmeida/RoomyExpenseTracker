package com.RoomyExpense.tracker.controller;

import com.RoomyExpense.tracker.model.User;
import com.RoomyExpense.tracker.service.IUserService;
import com.RoomyExpense.tracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    IUserService userService;

    //add Response Entity to controllers
    @GetMapping("/getAll")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/getById/{id}")
    public Optional<User> getUserByid(@PathVariable Long id){
        return userService.getUserById(id);
    }

    @PostMapping("/saveUser")
    public User saveUser(@RequestBody User user){
        return userService.saveUser(user);
    }

    @DeleteMapping("/deleteById/{id}")
    public void deleteUserById(@PathVariable Long id){
        userService.deleteUser(id);
    }





}
