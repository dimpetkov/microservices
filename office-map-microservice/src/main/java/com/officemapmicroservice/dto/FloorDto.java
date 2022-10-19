package com.officemapmicroservice.dto;

import com.googlecode.jmapper.annotations.JMap;
import com.googlecode.jmapper.annotations.JMapConversion;
import com.officemapmicroservice.entities.Room;

import java.util.ArrayList;
import java.util.List;

public class FloorDto {

    @JMap("floorId")
    private Long floorId;

    @JMap("floor")
    private int floorNumber;

    @JMap("rooms")
    private List<String> roomsList;

    @JMapConversion(from={"rooms"}, to={"roomsList"})
    public List<String> conversion(List<Room> rooms) {
        List<String> roomsNameList = new ArrayList<>();
        if (!rooms.isEmpty()) {
            rooms.forEach(building -> roomsNameList.add(building.getRoomName()));
        }
        return roomsNameList;
    }
}
