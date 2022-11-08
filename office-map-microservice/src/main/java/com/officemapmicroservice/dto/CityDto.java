package com.officemapmicroservice.dto;

import com.googlecode.jmapper.annotations.JMap;
import com.googlecode.jmapper.annotations.JMapConversion;
import com.officemapmicroservice.entities.Building;

import java.util.ArrayList;
import java.util.List;

public class CityDto {

    @JMap
    private Long cityId;

    @JMap
    private String cityName;

    @JMap("buildings")
    private List<String> buildingsDtoList;

    public CityDto(Long cityId, String cityName, List<String> buildingsDtoList) {
        this.cityId = cityId;
        this.cityName = cityName;
        this.buildingsDtoList = buildingsDtoList;
    }

    public CityDto() {
    }


    @JMapConversion(from={"buildings"}, to={"buildingsDtoList"})
    public List<String> conversion(List<Building> buildings) {
            List<String> buildingsNameList = new ArrayList<>();
        if(!buildings.isEmpty()) {
                    buildings.forEach(building -> buildingsNameList.add(building.getBuildingName()));
        }
            return buildingsNameList;
    }

    public Long getCityId() {
        return cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public List<String> getBuildingsDtoList() {
        return buildingsDtoList;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setBuildingsDtoList(List<String> buildingsDtoList) {
        this.buildingsDtoList = buildingsDtoList;
    }
}
