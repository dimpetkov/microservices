package com.officemapmicroservice.entities;

import javax.persistence.*;

@Entity
@Table(name = "buildings")
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long buildingId;
    @ManyToOne
    @JoinColumn(name = "city_city_id")
    private City city;
    private String buildingName;

    public City getCity() {
        return city;
    }
}
