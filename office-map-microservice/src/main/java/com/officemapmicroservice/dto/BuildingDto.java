package com.officemapmicroservice.dto;

import com.googlecode.jmapper.annotations.JMap;
import com.googlecode.jmapper.annotations.JMapConversion;
import com.officemapmicroservice.entities.Floor;

import java.util.List;

public class BuildingDto {

    @JMap("buildingId")
    private Long buildingId;

    @JMap("buildingName")
    private String buildingName;

    @JMap("floors")
    private int floorsCount;

    @JMapConversion(from={"floors"}, to={"floorsCount"})
    public int conversion(List<Floor> floors) {
        return floors.size();
    }
}
