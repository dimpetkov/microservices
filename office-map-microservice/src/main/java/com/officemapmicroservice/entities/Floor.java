package com.officemapmicroservice.entities;

import javax.persistence.*;

@Entity
@Table(name = "floors")
public class Floor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long floorId;
    @ManyToOne
    @JoinColumn(name = "building_building_id")
    private Building building;
    private int floor;

    public Building getBuilding() {
        return building;
    }

}
