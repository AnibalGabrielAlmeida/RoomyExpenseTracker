package com.RoomyExpense.tracker.mapper;

import com.RoomyExpense.tracker.DTO.HouseCreationDTO;
import com.RoomyExpense.tracker.DTO.HouseDTO;
import com.RoomyExpense.tracker.model.User;
import com.RoomyExpense.tracker.model.House;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class HouseMapper {

        public House toEntity(HouseCreationDTO dto) {
            House house = new House();
            house.setName(dto.getName());
            house.setAddress(dto.getAddress());
            return house;
        }

        public House DTOToEntity(HouseDTO dto){
            House house = new House();
            house.setName(dto.getName());
            house.setAddress(dto.getAddress());
            return house;
        }
        public HouseDTO toDTO(House house) {
            return new HouseDTO(house.getId(), house.getName(), house.getAddress(), List.of());
        }



    public HouseDTO toDTOWithRoommates(House house) {
        return new HouseDTO(
                house.getId(),
                house.getName(),
                house.getAddress(),
                house.getRoommates().stream()
                        .map(User::getName)
                        .collect(Collectors.toList())
        );
    }

    }


