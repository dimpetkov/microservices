package com.officemapmicroservice.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "buildings")
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long buildingId;
    @ManyToOne
    @JoinColumn(name = "city_id")
    private City cityName;
    private String buildingName;
    @OneToMany(mappedBy = "building")
    private List<Floor> floors;

    public City getCity() {
        return cityName;
    }

    public String getBuildingName() {
        return buildingName;
    }
}
