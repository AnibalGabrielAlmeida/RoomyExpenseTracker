package com.RoomyExpense.tracker.service;

import com.RoomyExpense.tracker.model.House;
import com.RoomyExpense.tracker.model.User;
import com.RoomyExpense.tracker.repository.HouseRepository;
import com.RoomyExpense.tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HouseService implements IHouseService{


    @Autowired
    private HouseRepository houseRepository;


    @Override
    public House saveHouse(House house){

        return houseRepository.save(house);
    }

    @Override
    public List<House> getAllHouses() {
        return houseRepository.findAll();
    }
    //check optional handling
    public Optional<House> getHouseById(Long id) {
        return houseRepository.findById(id);
    }

    @Override
    public void deleteHouse(Long id) {
        houseRepository.deleteById(id);
    }

    @Override
    public List<User> getRoommatesByHouseId(Long houseId) {
        return houseRepository.findRoommatesByHouseId(houseId);
    }
}
