package com.RoomyExpense.tracker.controller;

import com.RoomyExpense.tracker.model.House;
import com.RoomyExpense.tracker.model.User;
import com.RoomyExpense.tracker.service.IHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/house")
public class HouseController {

    @Autowired
    IHouseService houseService;

    //add Response Entity to controllers
    @GetMapping("/getAll")
    public List<House> getAllHouses() {
        return houseService.getAllHouses();
    }

    @GetMapping("/getById/{id}")
    public Optional<House> getHouseByid(@PathVariable Long id) {
        return houseService.getHouseById(id);
    }

    @PostMapping("/saveHouse")
    public House saveHouse(@RequestBody House house) {
        return houseService.saveHouse(house);
    }

    @DeleteMapping("/deleteById/{id}")
    public void deleteHouseById(@PathVariable Long id) {
        houseService.deleteHouse(id);
    }

}
