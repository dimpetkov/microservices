package com.officemapmicroservice.dto;

import com.googlecode.jmapper.annotations.JMap;
import com.googlecode.jmapper.annotations.JMapConversion;
import com.officemapmicroservice.entities.Desk;

import java.util.List;

public class RoomDto {

    @JMap("roomId")
    private Long roomId;

    @JMap("roomName")
    private String roomName;

    @JMap("desks")
    private int desksCount;

    @JMapConversion(from={"desks"}, to={"desksCount"})
    public int conversion(List<Desk> desks) {
        return desks.size();
    }
}
