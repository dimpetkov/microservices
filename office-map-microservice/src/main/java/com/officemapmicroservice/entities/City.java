package com.officemapmicroservice.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "cities")
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long cityId;
    private String cityName;
    @OneToMany(mappedBy = "cityName")
    private List<Building> buildings;

    public City() {
    }

    public City(Long cityId, String cityName) {
        this.cityId = cityId;
        this.cityName = cityName;
    }

    public Long getCityId() {
        return cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public List<Building> getBuildings() {
        return buildings;
    }
}
