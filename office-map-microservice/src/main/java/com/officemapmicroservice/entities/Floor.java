package com.officemapmicroservice.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "floors")
public class Floor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long floorId;
    @ManyToOne
    @JoinColumn(name = "building_id")
    private Building building;
    private int floor;
    @OneToMany(mappedBy = "floor")
    private List<Room> rooms;

    public Building getBuilding() {
        return building;
    }

}
